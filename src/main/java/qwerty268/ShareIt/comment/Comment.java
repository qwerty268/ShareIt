package qwerty268.ShareIt.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@Table(name = "Comments")
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "author_id")
    private Long authorId;
}
