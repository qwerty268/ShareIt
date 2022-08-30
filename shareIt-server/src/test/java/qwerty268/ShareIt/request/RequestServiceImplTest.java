package qwerty268.ShareIt.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.ItemRepository;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserRepository;
import qwerty268.ShareIt.user.exceptions.UserNotFoundException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = RequestServiceImpl.class)
@SpringJUnitConfig
class RequestServiceImplTest {
    @Autowired
    private RequestService requestService;

    @MockBean
    RequestRepository requestRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRepository itemRepository;

    private final RequestDTO requestDTO = new RequestDTO(1L, "Как же я хочу дрель", 1L, null);
    private final User user = new User(1L, "Иван Дрель", "aaaaaaaк@mial.ru");
    private final Instant instant = Instant.now();

    @BeforeEach
    public void createMockBehavior() {
        Request request = RequestMapper.fromDTO(requestDTO);
        request.setCreated(Date.from(instant));
        Mockito.when(requestRepository.save(any())).thenReturn(request);
        Mockito.when(userRepository.findById(requestDTO.getRequestorId())).thenReturn(Optional.ofNullable(user));
    }

    @Test
    void addRequest() {
        RequestDTO DTOToAdd = requestService.addRequest(requestDTO, requestDTO.getRequestorId());
        RequestDTO test = new RequestDTO(1L, "Как же я хочу дрель", 1L, Date.from(instant));
        assertEquals(DTOToAdd, test);
    }

    @Test
    void addRequestWithWrongUser() {
        Mockito.when(userRepository.findById(requestDTO.getRequestorId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> requestService.addRequest(requestDTO, requestDTO.getRequestorId()));
    }

}