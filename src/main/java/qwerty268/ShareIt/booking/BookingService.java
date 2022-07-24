package qwerty268.ShareIt.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingDTO addBooking(BookingDTO bookingDTO, Long userId) {
        Booking booking = BookingMapper.fromDTO(bookingDTO);
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING);

        booking = bookingRepository.save(booking);

        return BookingMapper.toDTO(booking);
    }
}
