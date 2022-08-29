package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import qwerty268.ShareIt.client.BaseClient;
import qwerty268.ShareIt.comment.CommentDTO;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {


    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        /* .requestFactory(HttpComponentsClientHttpRequestFactory::new)*/
                        .build()
        );
    }

    public ResponseEntity<Object> save(ItemDTO itemDTO, Long userId) {
        return post("", userId, itemDTO);
    }

    public ResponseEntity<Object> update(ItemDTO itemDTO, Long userId, Long itemId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return patch("/{itemId}", userId, parameters, itemDTO);
    }

    public ResponseEntity<Object> findAll(Long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> findById(Long itemId, Long bookerId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return get("/{itemId}", bookerId, parameters);
    }

    public void deleteById(Long itemId, Long userId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        delete("/{itemId}", userId, parameters);
    }

    public ResponseEntity<Object> findItemsByParam(String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addComment(CommentDTO commentDTO, Long userId, Long itemId) {
        Map<String, Object> parameters = Map.of(
                "itemId", itemId
        );
        return post("/{itemId}/comment", userId, parameters, commentDTO);
    }
}
