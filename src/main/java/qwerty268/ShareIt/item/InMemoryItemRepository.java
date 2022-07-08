package qwerty268.ShareIt.item;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();

    @Override
    public void save(Item item) {
        items.put(item.getId(), item);
    }

    @Override
    public void update(Item item) {
        if (items.get(item.getId()) != null) {
            items.put(item.getId(), item);
        }
    }

    @Override
    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findAll() {
        return (List<Item>) items.values();
    }

    @Override
    public void deleteById(Long itemId) {
        items.remove(itemId);
    }
}
