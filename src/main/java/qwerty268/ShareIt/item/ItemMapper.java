package qwerty268.ShareIt.item;

public class ItemMapper {
    public static ItemDTO toDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(),
                item.getRequest());
    }

    public static Item fromDTO(ItemDTO itemDTO) {
        return new Item(itemDTO.getId(), itemDTO.getName(), itemDTO.getDescription(), itemDTO.getAvailable(), itemDTO.getOwner(),
                itemDTO.getRequest());
    }

    public static Item update(Item notUpdatedItem, Item updatedItem) {
        if (updatedItem.getName() == null) {
            updatedItem.setName(notUpdatedItem.getName());
        }
        if (updatedItem.getDescription() == null) {
            updatedItem.setDescription(notUpdatedItem.getDescription());
        }
        if (updatedItem.getAvailable() == null) {
            updatedItem.setAvailable(notUpdatedItem.getAvailable());
        }
        if (updatedItem.getOwner() == null) {
            updatedItem.setOwner(notUpdatedItem.getOwner());
        }
        if (updatedItem.getRequest() == null) {
            updatedItem.setRequest(notUpdatedItem.getRequest());
        }
        updatedItem.setId(notUpdatedItem.getId());

        return updatedItem;
    }
}
