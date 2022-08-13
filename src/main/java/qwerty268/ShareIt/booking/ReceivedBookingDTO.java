package qwerty268.ShareIt.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ReceivedBookingDTO {
    private Long itemId;
    private Date start;
    private Date end;
}
