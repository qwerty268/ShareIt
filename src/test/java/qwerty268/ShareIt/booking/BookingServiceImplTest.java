package qwerty268.ShareIt.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.item.ItemRepository;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserRepository;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = BookingServiceImpl.class)
@SpringJUnitConfig
class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;

    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;


    private final Instant instant = Instant.now();
    private final Instant startInst = instant.plusSeconds(3600L);
    private final Instant endInst = instant.plusSeconds(7200L);

    private final Date start = Date.from(startInst);
    private final Date end = Date.from(endInst);
    private final Booking booking = new Booking(1L, start,
            end, 1L, 1L, null);
    private final ReceivedBookingDTO receivedBookingDTO = createReceivedDTOFromBooking(booking);


    private final User user = new User(
            1L,
            "John",
            "john.doe@mail.com");
    private final Item item = new Item(1L, "Lamp", "electric lamp", true, null, 1L);


    @BeforeEach
    public void addMockBehavior() {
        Mockito.when(bookingRepository.save(any())).thenReturn(booking);
        Mockito.when(bookingRepository.findById(booking.getItemId())).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.findById(booking.getBookerId())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(booking.getItemId())).thenReturn(Optional.of(item));
    }

    @Test
    void addBooking() {
        BookingDTO bookingDTO1 = BookingMapper.toDTO(booking, user, item);
        BookingDTO bookingDTO2 = bookingService.addBooking(receivedBookingDTO, user.getId());
        assertEquals(bookingDTO1, bookingDTO2);
    }

    @Test
    void updateToApproved() {
        booking.setStatus(Status.WAITING);
        BookingDTO bookingDTO = bookingService.update(user.getId(), true, 1L);
        assertEquals(bookingDTO.getStatus(), Status.APPROVED);
    }

    @Test
    void updateToRejected() {
        booking.setStatus(Status.WAITING);
        BookingDTO bookingDTO = bookingService.update(user.getId(), false, 1L);
        assertEquals(bookingDTO.getStatus(), Status.REJECTED);
    }

    private ReceivedBookingDTO createReceivedDTOFromBooking(Booking booking) {
        return new ReceivedBookingDTO(booking.getItemId(), booking.getStart(), booking.getEnd());
    }
}