package qwerty268.ShareIt.item;

public class ItemMapper {
    public static ItemDTO toDTO(Item item) {
        return new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner(),
                item.getRequest());
    }
}
