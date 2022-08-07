package qwerty268.ShareIt.user;

import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    UserDTO add(UserDTO userDTO);

    UserDTO update(UserDTO userDTO, Long userId);

    List<UserDTO> getAll();

    UserDTO getById(Long userId);

    void deleteById(Long userId);
}
