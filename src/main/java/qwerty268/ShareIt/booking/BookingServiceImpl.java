package qwerty268.ShareIt.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.booking.exceptions.BookingAlreadyPatchedException;
import qwerty268.ShareIt.booking.exceptions.BookingNotFoundException;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.item.ItemRepository;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.item.exceptions.ItemIsNotAvailable;
import qwerty268.ShareIt.item.exceptions.ItemNotFoundException;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserRepository;
import qwerty268.ShareIt.user.exceptions.UserDoesNotExistException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public BookingDTO addBooking(ReceivedBookingDTO bookingDTO, Long bookerId) {
        Booking booking = BookingMapper.fromDTO(bookingDTO);
        booking.setBookerId(bookerId);
        booking.setStatus(Status.WAITING);

        User booker = userRepository.findById(bookerId).orElseThrow(UserDoesNotExistException::new);
        Item item = itemRepository.findItemById(booking.getItemId()).orElseThrow(ItemNotFoundException::new);
        validate(booking, booker, item);


        if (item.getIsAvailable() == Boolean.FALSE) {
            throw new ItemIsNotAvailable();
        }

        booking = bookingRepository.save(booking);

        return createBookingDTO(booking);
    }

    @Transactional
    @Override
    public BookingDTO update(Long ownerId, Boolean status, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(InvalidArgsException::new);
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(InvalidArgsException::new);

        if (item.getOwnerId() != ownerId) {
            throw new InvalidOwnerOfItemException();
        }

        //Если уже обновляли
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BookingAlreadyPatchedException();
        }

        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        bookingRepository.save(booking);

        return createBookingDTO(booking);
    }

    @Override
    public BookingDTO getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(InvalidArgsException::new);
        if (item.getOwnerId() == userId || booking.getBookerId() == userId) {

            return createBookingDTO(booking);
        }

        throw new BookingNotFoundException();
    }


    @Override
    public List<BookingDTO> getBookingsOfUser(String state, Long userId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingRepository.findBookingsByBookerId(userId).forEach(booking ->
                        addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "FUTURE":
                bookingRepository.findBookingByStartAfterAndBookerId(Timestamp.from(Instant.now()), userId)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "CURRENT":
                Timestamp timestamp = Timestamp.from(Instant.now());
                bookingRepository.findBookingsByStartBeforeAndEndAfterAndBookerId(timestamp, timestamp, userId)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "PAST":
            case "WAITING":
            case "REJECTED":
                bookingRepository.findBookingsByStatusEqualsIgnoreCaseAndBookerId(Status.valueOf(state), userId)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            default:
                throw new InvalidArgsException();
        }
    }

    @Override
    public List<BookingDTO> getBookingsForOwner(String state, Long userId) {

        List<Long> ids = new ArrayList<>();
        itemRepository.findAllByOwnerId(userId).forEach(itemShort -> ids.add(itemShort.getId()));

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingRepository.findBookingsByItemIdIn(ids)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "FUTURE":
                bookingRepository.findBookingsByItemIdInAndStartAfter(ids, Timestamp.from(Instant.now()))
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "CURRENT":
                Timestamp timestamp = Timestamp.from(Instant.now());
                bookingRepository.findBookingsByItemIdInAndStartBeforeAndEndAfter(ids, timestamp, timestamp)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "PAST":
                bookingRepository.findBookingsByItemIdInAndEndBefore(ids, Timestamp.from(Instant.now()))
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            case "WAITING":
            case "REJECTED":
                bookingRepository.findBookingsByStatusAndOwnerId(state, userId)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                return bookingDTOS;
            default:
                throw new InvalidArgsException();
        }
    }

    private void addBookingDTO(List<BookingDTO> bookingDTOS, Booking booking) {
        bookingDTOS.add(createBookingDTO(booking));
    }

    private BookingDTO createBookingDTO(Booking booking) {
        return BookingMapper
                .toDTO(booking, userRepository.findById(booking.getBookerId()).orElseThrow(InvalidArgsException::new),
                        itemRepository.findItemById(booking.getItemId()).orElseThrow(InvalidArgsException::new));
    }

    private void validate(Booking booking, User booker, Item item) {
        if (booking.getEnd().before(Date.from(Instant.now())) ||
                booking.getEnd().before(booking.getStart()) ||
                booking.getStart().before(Date.from(Instant.now()))) {
            throw new InvalidArgsException();
        }

        if (booker.getId() == item.getOwnerId()) {
            throw new InvalidOwnerOfItemException();
        }
    }
}
