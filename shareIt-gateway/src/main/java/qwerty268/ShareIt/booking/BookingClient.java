package qwerty268.ShareIt.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import qwerty268.ShareIt.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(ReceivedBookingDTO bookingDTO, long userId) {
        return post("", userId, bookingDTO);
    }

    public ResponseEntity<Object> update(long userId, boolean approved, long bookingId) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );

        return patch("/{bookingId}?approved={approved}", userId) ;
    }

    public ResponseEntity<Object> getBooking(long userId, long bookingId) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId
        );
        return get("/{bookingId}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsOfUser(String state, long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsForOwner(String state, long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

}
