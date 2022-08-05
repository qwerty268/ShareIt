package qwerty268.ShareIt.item;

import qwerty268.ShareIt.comment.CommentDTO;

import java.util.List;

public interface ItemService {
    ItemDTO save(ItemDTO itemDTO, Long userId);

    ItemDTO update(ItemDTO itemDTO, Long userId, Long itemId);

    List<ItemWithBookingsAndCommentsDTO> findAll(Long userId, int from, int size);

    ItemWithBookingsAndCommentsDTO findById(Long itemId, Long bookerId);

    void deleteById(Long itemId, Long userId);

    List<ItemDTO> findItemsByParam(String text, int from, int size);

    CommentDTO addComment(CommentDTO commentDTO, Long userId, Long itemId);
}
