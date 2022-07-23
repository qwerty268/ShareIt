package qwerty268.ShareIt.booking;

import lombok.Data;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.user.User;

import java.util.Date;

@Data
public class BookingDTO {
    private Long id;
    private Date start;
    private Date end;
    private Long itemId;
    private Long bookerId;
    private Status status;

    public BookingDTO(Long id, Date start, Date end, Long itemId, Long bookerId, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }
}
