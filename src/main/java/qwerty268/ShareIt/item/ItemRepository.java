package qwerty268.ShareIt.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    void save(Item item, Long userId);
    Optional<Item> findById(Long itemId, Long userId);

    void update(Item item, Long userId);

    //поиск всех вещей пользователя
    List<Item> findAllItemsOfUser(Long userId);

    List<Item> findAll();


    void deleteById(Long itemId, Long userId);
}
