package qwerty268.ShareIt.booking;

import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.user.User;

public class BookingMapper {
    public static BookingDTO toDTO(Booking booking, User booker, Item item) {
        return new BookingDTO(booking.getId(), booking.getStart(), booking.getEnd(), item,
                booker, booking.getStatus());
    }

    public static Booking fromDTO(ReceivedBookingDTO bookingDTO) {
        return new Booking(bookingDTO.getItemId(),bookingDTO.getStart(), bookingDTO.getEnd());
    }
}
