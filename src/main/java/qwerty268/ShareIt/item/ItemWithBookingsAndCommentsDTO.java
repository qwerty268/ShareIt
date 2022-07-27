package qwerty268.ShareIt.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.booking.Booking;
import qwerty268.ShareIt.comment.Comment;
import qwerty268.ShareIt.comment.CommentDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemWithBookingsAndCommentsDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
    private Boolean isAvailable;
    private Long requestId;
    private Long ownerId;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDTO> comments;
}
