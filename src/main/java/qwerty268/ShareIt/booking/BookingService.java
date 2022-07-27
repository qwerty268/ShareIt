package qwerty268.ShareIt.booking;

import java.util.List;

public interface BookingService {
    BookingDTO addBooking(ReceivedBookingDTO bookingDTO, Long userId);

    BookingDTO update(Long userId, Boolean status, Long bookingId);

    BookingDTO getBooking(Long userId, Long bookingId);

    List<BookingDTO> getBookingsOfUser(String state, Long userId);

    List<BookingDTO> getBookingsForOwner(String state, Long userId);
}
