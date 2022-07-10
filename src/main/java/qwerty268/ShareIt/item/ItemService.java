package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.user.exceptions.UserDoesNotExistException;
import qwerty268.ShareIt.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Long id = 1L;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ItemDTO save(Item item, Long userId) {
        userRepository.getById(userId).orElseThrow(UserDoesNotExistException::new);
        validate(item);
        addId(item);

        itemRepository.save(item, userId);
        return ItemMapper.toDTO(item);
    }

    public ItemDTO update(Item item, Long userId, Long itemId) {
        Item notUpdatedItem = itemRepository.findById(itemId, userId).orElseThrow(InvalidOwnerOfItemException::new);

        Item updatedItem = ItemMapper.update(notUpdatedItem, item);

        itemRepository.update(updatedItem, userId);
        return ItemMapper.toDTO(updatedItem);
    }

    public List<ItemDTO> findAll(Long userId) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        itemRepository.findAllItemsOfUser(userId).forEach((item) -> itemDTOS.add(ItemMapper.toDTO(item)));

        return itemDTOS;
    }

    public ItemDTO findById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId, userId).orElseThrow();

        return ItemMapper.toDTO(item);
    }

    public ItemDTO findById(Long itemId) {
        List<Item> items = itemRepository.findAll();


        for (Item item : items) {
            if (item.getId() == itemId) {
                return ItemMapper.toDTO(item);
            }
        }

        return null;
    }

    public void deleteById(Long itemId, Long userId) {
        itemRepository.deleteById(itemId, userId);
    }

    public List<ItemDTO> findItemsByParam(String text) {
        if (text.equals("")) {
            return List.of();
        }

        List<Item> items = itemRepository.findAll().stream().filter(item ->
                (item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable()
        ).collect(Collectors.toList());

        List<ItemDTO> itemDTOS = new ArrayList<>();
        items.forEach(item -> itemDTOS.add(ItemMapper.toDTO(item)));

        return itemDTOS;
    }

    private void addId(Item item) {
        item.setId(id);
        id++;
    }


    private void validate(Item item) {
        if (item.getAvailable() == null ||
                item.getName() == null ||
                item.getName().isBlank() ||
                item.getDescription() == null) {
            throw new InvalidArgsException();
        }
    }
}
