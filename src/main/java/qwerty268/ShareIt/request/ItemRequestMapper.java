package qwerty268.ShareIt.request;

public class ItemRequestMapper {
    public static ItemRequestDTO toDTO(ItemRequest itemRequest) {
        return new ItemRequestDTO(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getRequestor(),
                itemRequest.getCreated());
    }
}
