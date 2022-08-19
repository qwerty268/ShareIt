package qwerty268.ShareIt.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.ItemDTO;
import qwerty268.ShareIt.item.ItemMapper;
import qwerty268.ShareIt.item.ItemRepository;
import qwerty268.ShareIt.request.exceptions.RequestNotFoundException;
import qwerty268.ShareIt.user.UserRepository;
import qwerty268.ShareIt.user.exceptions.UserNotFoundException;

import javax.transaction.Transactional;
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
    @Transactional
    public RequestDTO addRequest(RequestDTO requestDTO, Long requestorId) {
        checkUser(requestorId);
        validateRequest(requestDTO);

        requestDTO.setRequestorId(requestorId);

        Request request = RequestMapper.fromDTO(requestDTO);
        request.setCreated(Timestamp.from(Instant.now()));
        request = requestRepository.save(request);

        return RequestMapper.toDTO(request);
    }

    @Override
    public List<RequestDTO> getRequestsOfUser(Long requestorId) {
        checkUser(requestorId);

        List<RequestDTO> requestDTOS = new ArrayList<>();

        requestRepository.findRequestsByRequestorId(requestorId).forEach(request -> {
            RequestDTO requestDTO = createDTOWithItems(request);

            requestDTOS.add(requestDTO);
        });


        return requestDTOS;
    }

    @Override
    public List<RequestDTO> getAvailableRequests(Long userId, int from, int size) {
        checkUser(userId);

        List<RequestDTO> requestDTOS = new ArrayList<>();

        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());


        requestRepository.findRequestsByRequestorIdNot(userId, pageable).forEach(request -> {
            RequestDTO requestDTO = createDTOWithItems(request);
            requestDTOS.add(requestDTO);
        });

        return requestDTOS;
    }

    @Override
    public RequestDTO getRequestById(Long requestId, Long userId) {
        checkUser(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new);
        return createDTOWithItems(request);
    }

    private void checkUser(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void validateRequest(RequestDTO requestDTO) {
        if (requestDTO.getDescription() == null || Objects.equals(requestDTO.getDescription(), "")) {
            throw new InvalidArgsException();
        }
    }

    //добавление списка предметов реквесту
    private RequestDTO createDTOWithItems(Request request) {
        RequestDTO requestDTO = RequestMapper.toDTO(request);

        List<ItemDTO> itemDTOS = requestDTO.getItems();
        itemRepository.findItemsByRequestId(requestDTO.getId()).forEach(item -> //добавление списка предметов каждому реквесту
                itemDTOS.add(ItemMapper.toDTO(item)));

        return requestDTO;
    }
}
