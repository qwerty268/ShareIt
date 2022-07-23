package qwerty268.ShareIt.booking;

public class BookingMapper {
    public static BookingDTO toDTO(Booking booking) {
        return new BookingDTO(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItemId(),
                booking.getBookerId(), booking.getStatus());
    }
}
