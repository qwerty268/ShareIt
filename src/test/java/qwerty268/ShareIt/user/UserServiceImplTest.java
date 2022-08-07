package qwerty268.ShareIt.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import qwerty268.ShareIt.exception.InvalidArgsException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ContextConfiguration(classes = {UserServiceImpl.class})
@SpringJUnitConfig
class UserServiceImplTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    private final User user = new User(
            1L,
            "John",
            "john.doe@mail.com");


    @Test
    void add() {
        Mockito.when(userRepository.save(any())).thenReturn(user);
        assertEquals(UserMapper.toDTO(user), userService.add(UserMapper.toDTO(user)));
    }

    @Test
    void addUserWithFailEmail() {
        User newUser = new User(1L, "John", "notValid.com");


        assertThrows(InvalidArgsException.class, () -> userService.add(UserMapper.toDTO(newUser)));
    }

    @Test
    void update() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserDTO newUser = UserMapper.toDTO(new User(1L, "John", "Valid@mail.com"));

        Mockito.when(userRepository.save(any())).thenReturn(UserMapper.fromDTO(newUser));

        assertEquals(userService.update(newUser, newUser.getId()), newUser);

    }

    @Test
    void getAll() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDTO> userDTOS = userService.getAll();

        assertEquals(userDTOS.size(),1);
        assertEquals(userDTOS, List.of(UserMapper.toDTO(user)));
    }

    @Test
    void getById() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(userService.getById(user.getId()), UserMapper.toDTO(user));
    }
}