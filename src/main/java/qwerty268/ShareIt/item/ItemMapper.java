package qwerty268.ShareIt.item;

import qwerty268.ShareIt.booking.Booking;
import qwerty268.ShareIt.comment.Comment;

import java.util.List;

public class ItemMapper {
    public static ItemDTO toDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getIsAvailable(), item.getOwnerId(),
                item.getRequestId());
    }

    public static Item fromDTO(ItemDTO itemDTO, final Long ownerId) {

        return new Item(itemDTO.getId(), itemDTO.getName(), itemDTO.getDescription(), itemDTO.getIsAvailable(), ownerId,
                itemDTO.getRequestId());
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

    public static ItemWithBookingDatesAndCommentsDTO createDTOFromItemAndBookingAndComments(Item item, Booking booking,
                                                                                            List<Comment> comments) {
        return new ItemWithBookingDatesAndCommentsDTO(item.getId(), item.getName(), item.getDescription(),
                item.getIsAvailable(), item.getRequestId(), item.getOwnerId(), booking.getStart(), booking.getEnd(),
                comments);
    }

    public static ItemWithCommentsDTO createDTOFromItemAndComments(Item item, List<Comment> comments) {
        return new ItemWithCommentsDTO(item.getId(), item.getName(), item.getDescription(),
                item.getIsAvailable(), item.getRequestId(), item.getOwnerId(), comments);
    }
}
