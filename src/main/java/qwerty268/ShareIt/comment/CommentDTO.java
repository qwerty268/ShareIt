package qwerty268.ShareIt.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private Long itemId;
    private Long authorId;
    private String authorName;
    private Date created;
}
