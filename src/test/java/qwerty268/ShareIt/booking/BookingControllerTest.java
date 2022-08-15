package qwerty268.ShareIt.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.user.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class BookingControllerTest {


    @InjectMocks
    BookingController bookingController;
    @Mock
    BookingServiceImpl bookingService;


    MockMvc mvc;

    Instant instant = Instant.now();
    Instant startInst = instant.plusSeconds(3600L);
    Instant endInst = instant.plusSeconds(7200L);

    Date start = Date.from(startInst);
    Date end = Date.from(endInst);


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
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void updateBooking() throws Exception {
        Mockito.when(bookingService.update(any(), any(), any()))
                .thenReturn(BookingMapper.toDTO(booking, user, item));

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void getBooking() throws Exception {
        Mockito.when(bookingService.getBooking(any(), any()))
                .thenReturn(BookingMapper.toDTO(booking, user, item));

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class));
    }

    @Test
    void getBookingsOfUser() throws Exception {
        List<BookingDTO> bookingDTOS = List.of(BookingMapper.toDTO(booking, user, item));

        Mockito.when(bookingService.getBookingsOfUser(anyString(), anyLong(), anyInt(),  anyInt()))
                .thenReturn(bookingDTOS);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class));
    }

    @Test
    void getBookingsOfOwner() throws Exception {
        List<BookingDTO> bookingDTOS = List.of(BookingMapper.toDTO(booking, user, item));

        Mockito.when(bookingService.getBookingsForOwner(anyString(), anyLong(), anyInt(),  anyInt()))
                .thenReturn(bookingDTOS);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1L), Long.class));
    }

    private ReceivedBookingDTO createReceivedDTOFromBooking(Booking booking) {
        return new ReceivedBookingDTO(booking.getItemId(), booking.getStart(), booking.getEnd());
    }
}