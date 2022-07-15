package qwerty268.ShareIt.item;

import org.springframework.stereotype.Repository;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.item.exceptions.ItemNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    HashMap<Long, HashMap<Long, Item>> items = new HashMap<>();


    @Override
    public void save(Item item, Long userId) {
        HashMap<Long, Item> itemsOfUser = items.computeIfAbsent(userId, k -> new HashMap<>());

        itemsOfUser.put(item.getId(), item);
    }


    @Override
    public Optional<Item> findById(Long itemId, Long userId) {
        HashMap<Long, Item> itemsOfUser = items.get(userId);

        if (itemsOfUser == null) {
            throw new ItemNotFoundException();
        }


        return Optional.ofNullable(itemsOfUser.get(itemId));
    }

    @Override
    public void update(Item item, Long userId) {
        HashMap<Long, Item> itemsOfUser = items.get(userId);

        if (itemsOfUser != null) {
            if (itemsOfUser.get(item.getId()) != null) {
                itemsOfUser.put(item.getId(), item);
            } else {
                throw new InvalidOwnerOfItemException();
            }
        } else {
            throw new InvalidOwnerOfItemException();
        }

    }

    @Override
    public List<Item> findAll() {

        List<Item> itemList = new ArrayList<>();

        items.values().forEach(itemHashMap -> itemList.addAll(itemHashMap.values()));

        return itemList;
    }

    @Override
    public List<Item> findAllItemsOfUser(Long userId) {
        return new ArrayList<>(items.get(userId).values());
    }

    @Override
    public void deleteById(Long itemId, Long userId) {
        items.get(userId).remove(itemId);
    }

}
