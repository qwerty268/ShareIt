package qwerty268.ShareIt.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "Requests")
@Entity
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name = "requestor_id")
    private Long requestorId;
    private Timestamp creation;

    public Request(Long id, String description, Long requestorId) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
    }
}
