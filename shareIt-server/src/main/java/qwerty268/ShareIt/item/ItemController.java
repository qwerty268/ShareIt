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

    @PatchMapping("/items/{itemId}")
    @ResponseBody
    public ItemDTO updateItem(@PathVariable Long itemId, @RequestBody ItemDTO itemDTO,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.update(itemDTO, userId, itemId);
    }

    @GetMapping("/items")
    @ResponseBody
    public List<ItemWithBookingsAndCommentsDTO> findItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(defaultValue = "0", required = false) Integer from,
                                                          @RequestParam(defaultValue = "5", required = false) Integer size) {
        return service.findAll(userId, from, size);
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
    public List<ItemDTO> findItemsByParam(@RequestParam String text,
                                   @RequestParam(defaultValue = "0", required = false) Integer from,
                                   @RequestParam(defaultValue = "5", required = false) Integer size) {
        return service.findItemsByParam(text, from, size);
    }

    @PostMapping("/items/{itemId}/comment")
    @ResponseBody
    public CommentDTO addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody CommentDTO commentDTO,
                                 @PathVariable Long itemId) {
        return service.addComment(commentDTO, userId, itemId);
    }
}
