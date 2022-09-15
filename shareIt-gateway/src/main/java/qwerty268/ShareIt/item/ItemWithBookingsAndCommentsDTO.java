package qwerty268.ShareIt.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.booking.BookingDTO;
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
    private BookingDTO lastBooking;
    private BookingDTO nextBooking;
    private List<CommentDTO> comments;
}
