package qwerty268.ShareIt.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    @ResponseBody
    public BookingDTO addBooking(@RequestBody BookingDTO bookingDTO, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.addBooking(bookingDTO, userId);
    }

    @PatchMapping("/bookings/{bookingId}")
    @ResponseBody
    public void updateBooking( @RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam("approved") Boolean status, @PathVariable String bookingId) {

    }
}
