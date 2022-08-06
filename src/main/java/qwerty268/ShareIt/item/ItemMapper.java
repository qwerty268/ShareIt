package qwerty268.ShareIt.item;

import lombok.Data;
import qwerty268.ShareIt.booking.Booking;
import qwerty268.ShareIt.comment.Comment;
import qwerty268.ShareIt.comment.CommentDTO;

import java.util.Date;
import java.util.List;

public class ItemMapper {
    public static ItemDTO toDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getIsAvailable(),
                item.getRequestId(), item.getOwnerId());
    }

    public static Item fromDTO(ItemDTO itemDTO, final Long ownerId) {

        return new Item(itemDTO.getId(), itemDTO.getName(), itemDTO.getDescription(), itemDTO.getIsAvailable(),
                itemDTO.getRequestId(), ownerId);
    }

    public static Item update(Item notUpdatedItem, Item updatedItem) {
        if (updatedItem.getName() == null) {
            updatedItem.setName(notUpdatedItem.getName());
        }
        if (updatedItem.getDescription() == null) {
            updatedItem.setDescription(notUpdatedItem.getDescription());
        }
        if (updatedItem.getIsAvailable() == null) {
            updatedItem.setIsAvailable(notUpdatedItem.getIsAvailable());
        }
        if (updatedItem.getOwnerId() == null) {
            updatedItem.setOwnerId(notUpdatedItem.getOwnerId());
        }
        if (updatedItem.getRequestId() == null) {
            updatedItem.setRequestId(notUpdatedItem.getRequestId());
        }
        updatedItem.setId(notUpdatedItem.getId());

        return updatedItem;
    }

    public static ItemWithBookingsAndCommentsDTO createDTOFromItemBookingsComments(Item item,
                                                                                   Booking lastBooking,
                                                                                   Booking nextBooking,
                                                                                   List<CommentDTO> comments) {
        return new ItemWithBookingsAndCommentsDTO(item.getId(), item.getName(), item.getDescription(),
                item.getIsAvailable(), item.getRequestId(), item.getOwnerId(), lastBooking, nextBooking,
                comments);
    }

    public static ItemWithCommentsDTO createDTOFromItemAndComments(Item item, List<Comment> comments) {
        return new ItemWithCommentsDTO(item.getId(), item.getName(), item.getDescription(),
                item.getIsAvailable(), item.getRequestId(), item.getOwnerId(), comments);
    }

    public static ItemWithBookingDatesAndCommentsDTO createDTOFromItemBookingComments(Item item, Booking booking,
                                                                                      List<Comment> comments) {
        Date start = null;
        Date end = null;

        if (booking != null) {
            start = booking.getStart();
            end = booking.getEnd();
        }

        return new ItemWithBookingDatesAndCommentsDTO(item.getId(), item.getName(), item.getDescription(),
                item.getIsAvailable(), item.getRequestId(), item.getOwnerId(), start, end, comments);
    }
}
