package qwerty268.ShareIt.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.user.User;

import java.util.Date;

@Data
@AllArgsConstructor
public class BookingDTO {

    private Long id;
    private Date start;
    private Date end;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Item item;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User booker;
    private Status status;


}
