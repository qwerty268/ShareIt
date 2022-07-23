package qwerty268.ShareIt.request;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
public class RequestDTO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Long requestorId;


    public RequestDTO(Long id, String description, Long requestorId) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;

    }
}
