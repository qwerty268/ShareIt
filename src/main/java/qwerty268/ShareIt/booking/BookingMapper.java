package qwerty268.ShareIt.booking;

public class BookingMapper {
    public static BookingDTO toDTO(Booking booking) {
        return new BookingDTO(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItemId(),
                booking.getBookerId(), booking.getStatus());
    }

    public static Booking fromDTO(BookingDTO bookingDTO) {
        return new Booking(bookingDTO.getId(), bookingDTO.getStart(), bookingDTO.getEnd(), bookingDTO.getItemId(),
                bookingDTO.getBookerId(), bookingDTO.getStatus());
    }
}
