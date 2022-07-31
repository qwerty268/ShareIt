package qwerty268.ShareIt.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private Long itemId;
    private Long authorId;
    private String authorName;
    private Date created;
}
