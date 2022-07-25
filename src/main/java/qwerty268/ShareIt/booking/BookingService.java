package qwerty268.ShareIt.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.item.ItemRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public BookingDTO addBooking(BookingDTO bookingDTO, Long userId) {
        Booking booking = BookingMapper.fromDTO(bookingDTO);
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING);

        booking = bookingRepository.save(booking);

        return BookingMapper.toDTO(booking);
    }

    @Transactional
    public BookingDTO update(Long userId, Boolean status, Long bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        Item item = itemRepository.getById(booking.getItemId());

        if (item.getOwnerId() != userId) {
            throw new InvalidArgsException();
        }
        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        bookingRepository.save(booking);

        return BookingMapper.toDTO(booking);
    }

    public BookingDTO getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.getById(bookingId);
        Item item = itemRepository.getById(booking.getItemId());
        if (item.getOwnerId() == userId || booking.getBookerId() == userId) {
            return BookingMapper.toDTO(booking);
        }
        throw new InvalidArgsException();
    }


    public List<BookingDTO> getBookingsOfUser(String state, Long userId) {
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingRepository.findBookingsByBookerId(userId).forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "FUTURE":
                bookingRepository.findBookingByStartAfterAndBookerId(Timestamp.from(Instant.now()), userId)
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "CURRENT":
                Timestamp timestamp = Timestamp.from(Instant.now());
                bookingRepository.findBookingsByStartBeforeAndEndAfterAndBookerId(timestamp, timestamp, userId)
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "PAST":
            case "WAITING":
            case "REJECTED":
                bookingRepository.findBookingsByStatusEqualsIgnoreCaseAndBookerId(state.toString(), userId)
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            default:
                throw new InvalidArgsException();
        }
    }

    public List<BookingDTO> getBookingsForOwner(String state, Long userId) {

        List<Long> ids = new ArrayList<>();
        itemRepository.findAllByOwnerId(userId).forEach(itemShort -> ids.add(itemShort.getId()));

        List<BookingDTO> bookingDTOS = new ArrayList<>();
        switch (state) {
            case "ALL":
                bookingRepository.findBookingsByItemIdIn(ids)
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "FUTURE":
                bookingRepository.findBookingsByItemIdInAndStartAfter(ids, Timestamp.from(Instant.now()))
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "CURRENT":
                Timestamp timestamp = Timestamp.from(Instant.now());
                bookingRepository.findBookingsByItemIdInAndStartBeforeAndEndAfter(ids, timestamp, timestamp)
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "PAST":
                bookingRepository.findBookingsByItemIdInAndEndBefore(ids, Timestamp.from(Instant.now()))
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            case "WAITING":
            case "REJECTED":
                bookingRepository.findBookingsByStatusEqualsIgnoreCaseAndItemIdIn(state, userId)
                        .forEach(booking -> bookingDTOS.add(BookingMapper.toDTO(booking)));
                return bookingDTOS;
            default:
                throw new InvalidArgsException();
        }
    }
}
