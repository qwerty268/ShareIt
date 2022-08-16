package qwerty268.ShareIt.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.item.ItemRepository;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class BookingRepositoryTest {
    //почему-то не работает автоконфигурация TestEntityManager с DataJpaTest, поэтому так
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    Instant instant = Instant.now();
    Instant startInst = instant.plusSeconds(3600L);
    Instant endInst = instant.plusSeconds(7200L);

    Date start = Date.from(startInst);
    Date end = Date.from(endInst);

    User user = new User(1L, "dd", "sss");
    Item item = new Item(1L, "1", "1", true, null, 1L);
    Booking booking = new Booking(1L, start, end, 1L, 1L, Status.APPROVED);

    @Test
    void findBookingsByStatusAndOwnerId() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findBookingsByStatusAndOwnerId("APPROVED", 1L,
                Pageable.ofSize(1));
        assertEquals(bookings.get(0).getId(), booking.getId());
    }

    @Test
    void findApprovedBookingForOwnerByItemId() {
        userRepository.save(user);
        itemRepository.save(item);
        booking.setStart(Date.from(instant.minusSeconds(100L)));
        bookingRepository.save(booking);

        Booking booking1 = bookingRepository.findApprovedBookingForOwnerByItemId(1L, 1L);
        assertEquals(booking1.getId(), booking.getId());
    }

    @Test
    void findNextBookingForOwner() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findNextBookingsForOwner(1L, 1L);
        assertEquals(bookings.get(0).getId(), booking.getId());
    }
}