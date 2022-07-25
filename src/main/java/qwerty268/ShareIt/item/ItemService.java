package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.item.exceptions.ItemNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ItemService {
    private final ItemRepository itemRepository;


    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ItemDTO save(ItemDTO itemDTO, Long userId) {
        Item item = ItemMapper.fromDTO(itemDTO, userId);
        item.setOwnerId(userId);
        validate(item, userId);

        item = itemRepository.save(item);
        return ItemMapper.toDTO(item);
    }

    @Transactional
    public ItemDTO update(ItemDTO itemDTO, Long userId, Long itemId) {
        Item item = ItemMapper.fromDTO(itemDTO, userId);
        Item notUpdatedItem = itemRepository.findById(itemId).orElseThrow(InvalidOwnerOfItemException::new);
        Item updatedItem = ItemMapper.update(notUpdatedItem, item);

        validate(item, userId);

        itemRepository.save(updatedItem);
        return ItemMapper.toDTO(updatedItem);
    }

    public List<ItemDTO> findAll(Long userId) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        itemRepository.findItemsByOwnerId(userId).forEach((item) -> itemDTOS.add(ItemMapper.toDTO(item)));

        return itemDTOS;
    }


    public ItemDTO findById(Long itemId) {
        Item item = itemRepository.findItemById(itemId).orElseThrow(ItemNotFoundException::new);

        return ItemMapper.toDTO(item);
    }

    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    public List<ItemDTO> findItemsByParam(String text) {
        if (text.equals("")) {
            return List.of();
        }

        List<Item> items = itemRepository.search(text, text);

        List<ItemDTO> itemDTOS = new ArrayList<>();
        items.forEach(item -> itemDTOS.add(ItemMapper.toDTO(item)));

        return itemDTOS;
    }


    private void validate(Item item, Long userId) {
        if (item.getName() == null ||
                item.getName().isBlank() ||
                item.getDescription() == null ||
                !Objects.equals(item.getOwnerId(), userId)) {
            throw new InvalidArgsException();
        }
    }
}
