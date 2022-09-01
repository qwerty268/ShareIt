package qwerty268.ShareIt.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import qwerty268.ShareIt.client.BaseClient;

import java.util.Map;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> add(UserDTO userDTO) {
        return post("", userDTO);
    }

    public ResponseEntity<Object> update(UserDTO userDTO, Long userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return patch("/{userId}", null, parameters, userDTO);
    }

    public ResponseEntity<Object> getAll() {
        return get("");
    }

    public ResponseEntity<Object> getById(Long userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        return get("/{userId}", null, parameters);
    }

    void deleteById(Long userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );
        delete("/{userId}", null, parameters);
    }
}
