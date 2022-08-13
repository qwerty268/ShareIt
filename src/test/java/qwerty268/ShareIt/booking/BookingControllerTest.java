package qwerty268.ShareIt.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Autowired
    BookingController bookingController;
    @MockBean
    BookingService bookingService;

    MockMvc mvc;


    LocalDate localDateEnd = LocalDate.of(2022, 8, 20);
    Date end = Date.from(localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

    LocalDate localDateStart = LocalDate.of(2022, 8, 15);
    Date start = Date.from(localDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant());

    Booking booking = new Booking(1L, start,
            end, 1L, 1L, null);

    ReceivedBookingDTO bookingDTO = createReceivedDTOFromBooking(booking);

    User user = new User(
            1L,
            "John",
            "john.doe@mail.com");

    Item item = new Item(1L, "Lamp", "electric lamp", true, null, 1L);

    final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

    }

    @Test
    void addBooking() throws Exception {
        Mockito.when(bookingService.addBooking(bookingDTO, 1L))
                .thenReturn(BookingMapper.toDTO(booking, user, item));

        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(bookingDTO))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect()


    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getBookingsOfUser() {
    }

    @Test
    void getBookingsOfOwner() {
    }

    private ReceivedBookingDTO createReceivedDTOFromBooking(Booking booking) {
        return new ReceivedBookingDTO(booking.getItemId(), booking.getStart(), booking.getEnd());
    }
}