package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    private final ItemRepository repository;

    @Autowired
    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/items")
    public Item saveItem(@RequestBody Item item) {
        repository.save(item);
        return item;
    }

    @PutMapping("/items")
    public Item updateItem(@RequestBody Item item) {
        repository.update(item);
        return item;
    }

    @GetMapping("/items")
    public List<Item> findItems() {
        return repository.findAll();
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow();
    }
}
