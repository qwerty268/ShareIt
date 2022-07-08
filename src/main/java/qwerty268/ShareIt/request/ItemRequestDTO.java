package qwerty268.ShareIt.request;

import lombok.Data;
import qwerty268.ShareIt.user.User;

import java.util.Date;

@Data
public class ItemRequestDTO {
    private Long id;
    private String description;
    private User requestor;
    private Date created;

    public ItemRequestDTO(Long id, String description, User requestor, Date created) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }
}
