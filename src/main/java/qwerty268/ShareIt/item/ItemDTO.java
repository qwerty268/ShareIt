package qwerty268.ShareIt.item;

import lombok.Data;

@Data
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private String owner;
    private String request;

    public ItemDTO(Long id, String name, String description, Boolean available, String owner, String request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
