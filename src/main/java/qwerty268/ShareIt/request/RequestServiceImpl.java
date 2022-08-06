package qwerty268.ShareIt.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.ItemRepository;
import qwerty268.ShareIt.request.exceptions.RequestNotFoundException;
import qwerty268.ShareIt.user.UserRepository;
import qwerty268.ShareIt.user.exceptions.UserNotFoundException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public RequestDTO addRequest(RequestDTO requestDTO, Long requestorId) {
        checkUser(requestorId);
        validateRequest(requestDTO);

        requestDTO.setRequestorId(requestorId);

        Request request = RequestMapper.fromDTO(requestDTO);
        request = requestRepository.save(request);
        request.setCreated(Timestamp.from(Instant.now()));

        return RequestMapper.toDTO(request);
    }

    @Override
    public List<RequestDTO> getRequestsOfUser(Long requestorId) {
        checkUser(requestorId);

        List<RequestDTO> requestDTOS = new ArrayList<>();
        requestRepository.findRequestsByRequestorId(requestorId).forEach(request ->
                requestDTOS.add(RequestMapper.toDTO(request)));

        return requestDTOS;
    }

    @Override
    public List<RequestDTO> getAvailableRequests(Long userId, int from, int size) {
        checkUser(userId);

        List<RequestDTO> requestDTOS = new ArrayList<>();

        Pageable pageable = PageRequest.of(from, size, Sort.by("creation").descending());

        requestRepository.findRequestsByRequestorIdNot(userId, pageable).forEach(request ->
                requestDTOS.add(RequestMapper.toDTO(request)));
        return requestDTOS;
    }

    @Override
    public RequestDTO getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new);
        RequestDTO requestDTO = RequestMapper.toDTO(request);
        requestDTO.setResponses(itemRepository.findItemsByRequestId(requestId));
        return requestDTO;
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void validateRequest(RequestDTO requestDTO) {
        if (requestDTO.getDescription() == null || Objects.equals(requestDTO.getDescription(), "")) {
            throw new InvalidArgsException();
        }
    }
}
