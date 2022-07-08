package qwerty268.ShareIt.item;

import lombok.Data;

@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private String owner;
    private String request;
}
