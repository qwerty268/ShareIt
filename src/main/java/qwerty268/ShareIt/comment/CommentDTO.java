package qwerty268.ShareIt.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private Long itemId;
    private Long authorId;
}
