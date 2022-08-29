package qwerty268.ShareIt.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.item.ItemDTO;
import qwerty268.ShareIt.user.UserDTO;

import java.util.Date;

@Data
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Date start;
    private Date end;
    private ItemDTO item;
    private UserDTO booker;
    private Status status;
}
