package qwerty268.ShareIt.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedBookingDTO {
    private Long itemId;
    private Date start;
    private Date end;
}
