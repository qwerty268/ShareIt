package qwerty268.ShareIt.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.user.exceptions.UserDoesNotExistException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDTO add(UserDTO userDTO) {
        User user = UserMapper.fromDTO(userDTO);

        validate(user);

        user = repository.save(user);
        log.info("Пользователь сохранён");
        return UserMapper.toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO update(UserDTO userDTO, Long userId) {
        User user = UserMapper.fromDTO(userDTO);

        User notUpdatedUser = repository.getById(userId);

        UserMapper.updateUser(notUpdatedUser, user);

        user = repository.save(user);
        validate(user);
        log.info("Пользователь обновлён");
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = repository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        users.forEach(user -> userDTOS.add(UserMapper.toDTO(user)));
        log.info("Пользователи возвращены");
        return userDTOS;
    }

    @Override
    public UserDTO getById(Long userId) {
        UserDTO userDTO = UserMapper.toDTO(repository.findById(userId).orElseThrow(UserDoesNotExistException::new));
        log.info("Пользователь возвращён");
        return userDTO;
    }

    @Override
    public void deleteById(Long userId) {
        repository.deleteById(userId);
        log.info("Пользователь удалён");
    }


    private void validate(User user) {
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }
}



