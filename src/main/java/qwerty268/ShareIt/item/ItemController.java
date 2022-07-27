package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qwerty268.ShareIt.comment.CommentDTO;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping("/items")
    @ResponseBody
    public ItemDTO saveItem(@RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.save(itemDTO, userId);
    }

    @PatchMapping("/items/{id}")
    @ResponseBody
    public ItemDTO updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.update(itemDTO, userId, id);
    }

    @GetMapping("/items")
    @ResponseBody
    public List<ItemWithBookingDatesAndCommentsDTO> findItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/items/{itemId}")
    @ResponseBody
    public ItemWithBookingsAndCommentsDTO getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return service.findById(itemId, bookerId);
    }

    @DeleteMapping("/items/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        service.deleteById(itemId, userId);
    }

    @GetMapping("/items/search")
    @ResponseBody
    public List<ItemDTO> findItems(@RequestParam String text) {
        return service.findItemsByParam(text);
    }

    @PostMapping("/items/{itemId}/comment")
    @ResponseBody
    public CommentDTO addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody CommentDTO commentDTO) {
        return service.addComment(commentDTO, userId);
    }
}
