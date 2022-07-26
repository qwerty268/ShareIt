package qwerty268.ShareIt.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.comment.Comment;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemWithBookingDatesAndCommentsDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
    private Boolean isAvailable;
    private Long requestId;
    private Long ownerId;
    private Date start;
    private Date end;
    private List<Comment> comments;
}
