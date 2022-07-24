package qwerty268.ShareIt.item;

import lombok.Data;
import qwerty268.ShareIt.booking.BookingDTO;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
public class ItemDTO {
    private Long id;
    private String name;
    private String description;

    private Boolean isAvailable;
    private Long requestId;


    private Long ownerId;

    public ItemDTO(Long id, String name, String description, Boolean isAvailable, Long requestId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        this.requestId = requestId;
        this.ownerId = ownerId;
    }

}
