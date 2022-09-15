package qwerty268.ShareIt.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qwerty268.ShareIt.exceptions.InvalidArgsException;

import javax.validation.constraints.Min;
import java.util.Objects;

@Slf4j
@Validated
@RestController
public class RequestController {

    private final RequestClient requestClient;

    @Autowired
    public RequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping("/requests")
    public ResponseEntity<Object> addRequest(@RequestBody RequestDTO requestDTO,
                                             @RequestHeader("X-Sharer-User-Id") @Min(0) Long requestorId) {
        log.info("Получен post request");
        validateRequest(requestDTO);
        return requestClient.addRequest(requestDTO, requestorId);
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") @Min(0) Long requestorId) {
        log.info("Получен getRequestsOfUser request");
        return requestClient.getRequestsOfUser(requestorId);
    }

    @GetMapping("/requests/all")
    public ResponseEntity<Object> getAvailableRequests(@RequestHeader("X-Sharer-User-Id") @Min(0) Long userId,
                                                 @RequestParam(defaultValue = "0", required = false) Integer from,
                                                 @RequestParam(defaultValue = "5", required = false) Integer size) {
        log.info("Получен getAvailableRequests request");
        return requestClient.getAvailableRequests(userId, from, size);
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable @Min(0) Long requestId,
                                     @RequestHeader("X-Sharer-User-Id") @Min(0) Long userId) {
        log.info("Получен getRequestById request");
        return requestClient.getRequestById(requestId, userId);
    }

    private void validateRequest(RequestDTO requestDTO) {
        if (requestDTO.getDescription() == null || Objects.equals(requestDTO.getDescription(), "")) {
            log.info("InvalidArgsException");
            throw new InvalidArgsException();
        }
        log.info("Валидация пройдена");
    }
}
