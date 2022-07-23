package qwerty268.ShareIt.comment;

import qwerty268.ShareIt.user.UserDTO;

public class CommentMapper {
    public static CommentDTO toDTO(Comment comment) {
        return new CommentDTO(comment.getId(), comment.getText(), comment.getItemId(), comment.getAuthorId());
    }

    public static Comment fromDTO(CommentDTO commentDTO) {
        return new Comment(commentDTO.getId(), commentDTO.getText(), commentDTO.getItemId(), commentDTO.getAuthorId());
    }
}
