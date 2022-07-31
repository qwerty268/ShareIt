package qwerty268.ShareIt.comment;

import java.time.Instant;
import java.util.Date;

public class CommentMapper {
    public static CommentDTO toDTO(Comment comment, String authorName) {
        return new CommentDTO(comment.getId(), comment.getText(), comment.getItemId(), comment.getAuthorId(), authorName,
                comment.getCreated());
    }

    public static Comment fromDTO(CommentDTO commentDTO) {
        return new Comment(commentDTO.getId(), commentDTO.getText(), commentDTO.getItemId(), commentDTO.getAuthorId(),
                Date.from(Instant.now()));
    }
}
