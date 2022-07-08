package qwerty268.ShareIt.request;

import lombok.Data;
import qwerty268.ShareIt.user.User;

import java.util.Date;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private Date created;
}
