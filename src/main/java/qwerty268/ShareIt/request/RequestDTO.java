package qwerty268.ShareIt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import qwerty268.ShareIt.item.Item;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@AllArgsConstructor
public class RequestDTO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Long requestorId;
    private List<Item> responses;

    public RequestDTO(Long id, String description, Long requestorId) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;

    }

}
