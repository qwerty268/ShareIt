package qwerty268.ShareIt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qwerty268.ShareIt.item.Item;
import qwerty268.ShareIt.item.ItemDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Long requestorId;
    private Date created;
    private List<ItemDTO> items = new ArrayList<>();

    public RequestDTO(Long id, String description, Long requestorId, Date created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }

}
