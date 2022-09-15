package qwerty268.ShareIt.request;

import java.util.List;

public interface RequestService {
    RequestDTO addRequest(RequestDTO requestDTO, Long requestorId);

    List<RequestDTO> getRequestsOfUser(Long requestorId);

    List<RequestDTO> getAvailableRequests(Long userId, int from, int size);

    RequestDTO getRequestById(Long requestId, Long userId);
}
