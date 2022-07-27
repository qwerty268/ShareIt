package qwerty268.ShareIt.item;

import qwerty268.ShareIt.comment.CommentDTO;

import java.util.List;

public interface ItemService {
    ItemDTO save(ItemDTO itemDTO, Long userId);

    ItemDTO update(ItemDTO itemDTO, Long userId, Long itemId);

    List<ItemWithBookingDatesAndCommentsDTO> findAll(Long userId);

    ItemWithBookingsAndCommentsDTO findById(Long itemId, Long bookerId);

    void deleteById(Long itemId, Long userId);

    List<ItemDTO> findItemsByParam(String text);

    CommentDTO addComment(CommentDTO commentDTO, Long userId);
}
