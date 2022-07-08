package qwerty268.ShareIt.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    void save(Item item);

    Optional<Item> findById(Long itemId);

    void update(Item item);
    List<Item> findAll();

    void deleteById(Long itemId);
}
