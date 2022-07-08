package qwerty268.ShareIt.booking;

import lombok.Data;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.user.User;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Booking {
    private Long id;
    private Date start;
    private Date end;
    private Item item;
    private User booker;
    private Status status;
}

enum Status {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
