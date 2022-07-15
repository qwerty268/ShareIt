package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping("/items")
    public ItemDTO saveItem(@RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.save(itemDTO, userId);
    }

    @PatchMapping("/items/{id}")
    public ItemDTO updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.update(itemDTO, userId, id);
    }

    @GetMapping("/items")
    public List<ItemDTO> findItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/items/{id}")
    public ItemDTO getItem(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        service.deleteById(id, userId);
    }

    @GetMapping("/items/search")
    public List<ItemDTO> findItems(@RequestParam String text) {
        return service.findItemsByParam(text);
    }
}
