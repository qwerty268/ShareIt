package qwerty268.ShareIt.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qwerty268.ShareIt.exceptions.InvalidArgsException;

import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.Date;

@Slf4j
@RestController
@Validated
public class BookingController {
    private final BookingClient bookingClient;


    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping("/bookings")
    @ResponseBody
    public ResponseEntity<Object> addBooking(@RequestBody ReceivedBookingDTO bookingDTO,
                                 @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId) {
        validateBooking(bookingDTO);
        return bookingClient.addBooking(bookingDTO, userId);
    }

    @PatchMapping("/bookings/{bookingId}")
    @ResponseBody
    public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                    @RequestParam("approved") Boolean status, @PathVariable @Min(0) Long bookingId) {
        return bookingClient.update(userId, status, bookingId);
    }

    @GetMapping("/bookings/{bookingId}")
    @ResponseBody
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                 @PathVariable @Min(0) Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/bookings")
    @ResponseBody
    public ResponseEntity<Object> getBookingsOfUser(
            @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "5", required = false) Integer size) {
        return bookingClient.getBookingsOfUser(state, userId, from, size);
    }

    @GetMapping("/bookings/owner")
    @ResponseBody
    public ResponseEntity<Object> getBookingsOfOwner(
            @RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
            @RequestParam(defaultValue = "0", required = false) Integer from,
            @RequestParam(defaultValue = "5", required = false) Integer size) {
        return bookingClient.getBookingsForOwner(state, userId, from, size);
    }


    private void validateBooking(ReceivedBookingDTO booking) {
        if (booking.getEnd().before(Date.from(Instant.now())) ||
                booking.getEnd().before(booking.getStart()) ||
                booking.getStart().before(Date.from(Instant.now()))) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }
}
