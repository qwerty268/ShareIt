package qwerty268.ShareIt.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookingController {
    private final BookingService bookingService;


    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    @ResponseBody
    public BookingDTO addBooking(@RequestBody ReceivedBookingDTO bookingDTO, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.addBooking(bookingDTO, userId);
    }

    @PatchMapping("/bookings/{bookingId}")
    @ResponseBody
    public BookingDTO updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam("approved") Boolean status, @PathVariable Long bookingId) {
        return bookingService.update(userId, status, bookingId);
    }

    @GetMapping("/bookings/{bookingId}")
    @ResponseBody
    public BookingDTO getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping("/bookings")
    @ResponseBody
    public List<BookingDTO> getBookingsOfUser(
            @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingsOfUser(state, userId);
    }

    @GetMapping("/bookings/owner")
    @ResponseBody
    public List<BookingDTO> getBookingsOfOwner(
            @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingsForOwner(state, userId);
    }

}
