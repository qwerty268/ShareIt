package qwerty268.ShareIt.item;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "Items")
@Entity
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "owner_id")
    private Long ownerId;

    public Item(Long id, String name, String description, Boolean isAvailable, Long requestId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        this.requestId = requestId;
        this.ownerId = ownerId;
    }
}
