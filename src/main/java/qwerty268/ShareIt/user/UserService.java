package qwerty268.ShareIt.user;

import org.apache.commons.validator.routines.EmailValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.user.exceptions.UserAlreadyExistException;
import qwerty268.ShareIt.user.exceptions.UserDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository repository;
    private Long id = 1L;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public UserDTO add(UserDTO userDTO) {
        User user = UserMapper.fromDTO(userDTO);

        validate(user);

        addId(user);
        repository.save(user);

        return UserMapper.toDTO(user);
    }

    public UserDTO update(UserDTO userDTO, Long userId) {
        User user = UserMapper.fromDTO(userDTO);

        User notUpdatedUser = repository.getById(userId).orElseThrow(UserDoesNotExistException::new);

        User updatedUser = UserMapper.updateUser(notUpdatedUser, user);

        validate(updatedUser);

        repository.update(updatedUser);

        return UserMapper.toDTO(updatedUser);
    }

    public List<UserDTO> getAll() {
        List<User> users = repository.getAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        users.forEach(user -> userDTOS.add(UserMapper.toDTO(user)));

        return userDTOS;
    }

    public UserDTO getById(Long userId) {
        return UserMapper.toDTO(repository.getById(userId).orElseThrow());
    }

    public void deleteById(Long userId) {
        repository.deleteById(userId);
    }

    private void addId(User user) {
        user.setId(id);
        id++;
    }

    private void validate(User user) {
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new InvalidArgsException();
        }

       throwExceptionIfAlreadyExist(user);

    }

    private void throwExceptionIfAlreadyExist(User user) {
        repository.getAll().forEach((user1) -> {
            if (Objects.equals(user1.getEmail(), user.getEmail()) && !Objects.equals(user1.getId(), user.getId())) {
                throw new UserAlreadyExistException();
            }
        });
    }


}
