package qwerty268.ShareIt.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/requests")
    public RequestDTO addRequest(@RequestBody RequestDTO requestDTO, @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return requestService.addRequest(requestDTO, requestorId);
    }

    @GetMapping("/requests")
    public List<RequestDTO> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return requestService.getRequestsOfUser(requestorId);
    }

    @GetMapping("/requests/all")
    public List<RequestDTO> getAvailableRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "0", required = false) Integer from,
                                                 @RequestParam(defaultValue = "5", required = false) Integer size) {
        return requestService.getAvailableRequests(userId, from, size);
    }

    @GetMapping("/requests/{requestId}")
    public RequestDTO getRequestById(@PathVariable Long requestId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getRequestById(requestId, userId);
    }
}
