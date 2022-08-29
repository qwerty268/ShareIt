package qwerty268.ShareIt.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.comment.CommentDTO;

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
    private List<CommentDTO> comments;
}
