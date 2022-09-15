package qwerty268.ShareIt.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
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
