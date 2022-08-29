package qwerty268.ShareIt.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qwerty268.ShareIt.comment.CommentDTO;
import qwerty268.ShareIt.exceptions.InvalidArgsException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
public class ItemController {

    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient service) {
        this.itemClient = service;
    }

    @PostMapping("/items")
    @ResponseBody
    public ResponseEntity<Object> saveItem(@RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId) {
        validateItem(itemDTO);
        return itemClient.save(itemDTO, userId);
    }

    @PatchMapping("/items/{id}")
    @ResponseBody
    public ResponseEntity<Object> updateItem(@PathVariable @Min(0) Long id, @RequestBody ItemDTO itemDTO,
                              @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId) {
        validateItem(itemDTO);
        return itemClient.update(itemDTO, userId, id);
    }

    @GetMapping("/items")
    @ResponseBody
    public ResponseEntity<Object> findItems(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                                          @RequestParam(defaultValue = "0", required = false) Integer from,
                                                          @RequestParam(defaultValue = "5", required = false) Integer size) {
        return itemClient.findAll(userId, from, size);
    }

    @GetMapping("/items/{itemId}")
    @ResponseBody
    public ResponseEntity<Object> getItem(@PathVariable @Min(0) Long itemId,
                                                  @RequestHeader("X-Sharer-User-Id") @Min(0) Long bookerId) {
        return itemClient.findById(itemId, bookerId);
    }

    @DeleteMapping("/items/{itemId}")
    public void deleteItem(@PathVariable @Min(0) Long itemId, @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId) {
        itemClient.deleteById(itemId, userId);
    }

    @GetMapping("/items/search")
    @ResponseBody
    public ResponseEntity<Object> findItemsByParam(@RequestParam @NotBlank String text,
                                   @RequestParam(defaultValue = "0", required = false) Integer from,
                                   @RequestParam(defaultValue = "5", required = false) Integer size) {
        return itemClient.findItemsByParam(text, from, size);
    }

    @PostMapping("/items/{itemId}/comment")
    @ResponseBody
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                 @RequestBody CommentDTO commentDTO,
                                 @PathVariable @Min(0) Long itemId) {
        validateComment(commentDTO);
        return itemClient.addComment(commentDTO, userId, itemId);
    }

    private void validateItem(ItemDTO itemDTO) {
        if (itemDTO.getName() == null ||
                itemDTO.getName().isBlank() ||
                itemDTO.getDescription() == null ||
                itemDTO.getDescription().isBlank() ||
                itemDTO.getIsAvailable() == null) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }

    private void validateComment(CommentDTO commentDTO) {
        if (commentDTO.getText() == null || commentDTO.getText().isBlank()) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }
}
