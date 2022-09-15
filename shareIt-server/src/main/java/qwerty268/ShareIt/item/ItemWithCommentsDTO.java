package qwerty268.ShareIt.item;

import qwerty268.ShareIt.comment.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ItemWithCommentsDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
    private Boolean isAvailable;
    private Long requestId;
    private Long ownerId;
    private List<Comment> comments;
}
