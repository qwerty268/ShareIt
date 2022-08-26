package qwerty268.ShareIt.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import qwerty268.ShareIt.user.exceptions.UserNotFoundException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
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
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(ItemNotFoundException::new);
        validate(booking, booker, item);


        if (item.getIsAvailable() == Boolean.FALSE) {
            log.error("ItemIsNotAvailable");
            throw new ItemIsNotAvailable();
        }

        booking = bookingRepository.save(booking);
        log.info("Бронь сохрана");
        return createBookingDTO(booking);
    }

    @Transactional
    @Override
    public BookingDTO update(Long ownerId, Boolean status, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(InvalidArgsException::new);
        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(InvalidArgsException::new);

        if (!Objects.equals(item.getOwnerId(), ownerId)) {
            log.error("InvalidOwnerOfItemException");
            throw new InvalidOwnerOfItemException();
        }

        //Если уже обновляли
        if (booking.getStatus().equals(Status.APPROVED)) {
            log.error("BookingAlreadyPatchedException");
            throw new BookingAlreadyPatchedException();
        }

        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        bookingRepository.save(booking);
        log.info("Бронь обновлена");
        return createBookingDTO(booking);
    }

    @Override
    public BookingDTO getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        Item item = itemRepository.findById(booking.getItemId()).orElseThrow(InvalidArgsException::new);
        if (item.getOwnerId() == userId || booking.getBookerId() == userId) {
            log.info("Брони пользователя вохвращены");
            return createBookingDTO(booking);
        }

        throw new BookingNotFoundException();
    }


    @Override
    public List<BookingDTO> getBookingsOfUser(String state, Long userId, int from, int size) {
        validateUser(userId);

        Timestamp currentTimestamp = getCurrentTimestamp();
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingRepository
                        .findBookingsByBookerId(userId, pageable).forEach(booking ->
                                addBookingDTO(bookingDTOS, booking));
                log.info("Брони пользователя вохвращены");
                return bookingDTOS;
            case "FUTURE":
                bookingRepository
                        .findBookingByStartAfterAndBookerId(currentTimestamp, userId, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони пользователя вохвращены");
                return bookingDTOS;
            case "CURRENT":
                bookingRepository
                        .findBookingsByStartBeforeAndEndAfterAndBookerId(currentTimestamp, currentTimestamp, userId,
                                pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони пользователя вохвращены");
                return bookingDTOS;
            case "PAST":
                bookingRepository.findBookingByEndBeforeAndBookerId(currentTimestamp, userId, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони пользователя вохвращены");
                return bookingDTOS;
            case "WAITING":
            case "REJECTED":
                bookingRepository
                        .findBookingsByStatusEqualsIgnoreCaseAndBookerId(state, userId, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони пользователя вохвращены");
                return bookingDTOS;
            default:
                log.error("IllegalStateException");
                throw new IllegalStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDTO> getBookingsForOwner(String state, Long userId, int from, int size) {
        validateUser(userId);

        Timestamp currentTimestamp = getCurrentTimestamp();
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").descending());

        List<Long> ids = new ArrayList<>();
        itemRepository.findAllByOwnerId(userId).forEach(itemShort -> ids.add(itemShort.getId()));

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingRepository.findBookingsByItemIdIn(ids, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони для владельца вохвращены");
                return bookingDTOS;
            case "FUTURE":
                bookingRepository.findBookingsByItemIdInAndStartAfter(ids, currentTimestamp, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони для владельца вохвращены");
                return bookingDTOS;
            case "CURRENT":
                bookingRepository.findBookingsByItemIdInAndStartBeforeAndEndAfter(ids, currentTimestamp,
                                currentTimestamp, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони для владельца вохвращены");
                return bookingDTOS;
            case "PAST":
                bookingRepository.findBookingsByItemIdInAndEndBefore(ids, currentTimestamp, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони для владельца вохвращены");
                return bookingDTOS;
            case "WAITING":
            case "REJECTED":
                bookingRepository.findBookingsByStatusAndOwnerId(state, userId, pageable)
                        .forEach(booking -> addBookingDTO(bookingDTOS, booking));
                log.info("Брони для владельца вохвращены");
                return bookingDTOS;
            default:
                log.error("IllegalStateException");
                throw new IllegalStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void addBookingDTO(List<BookingDTO> bookingDTOS, Booking booking) {
        bookingDTOS.add(createBookingDTO(booking));
    }

    private BookingDTO createBookingDTO(Booking booking) {
        return BookingMapper
                .toDTO(booking, userRepository.findById(booking.getBookerId()).orElseThrow(InvalidArgsException::new),
                        itemRepository.findById(booking.getItemId()).orElseThrow(InvalidArgsException::new));
    }

    private void validate(Booking booking, User booker, Item item) {
        if (booking.getEnd().before(Date.from(Instant.now())) ||
                booking.getEnd().before(booking.getStart()) ||
                booking.getStart().before(Date.from(Instant.now()))) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }

        if (!Objects.equals(booker.getId(), item.getOwnerId())) {
            log.error("InvalidOwnerOfItemException");
            throw new InvalidOwnerOfItemException();
        }
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private Timestamp getCurrentTimestamp() {
        return Timestamp.from(Instant.now(Clock.system(ZoneId.of("Europe/Moscow"))));
    }
}
