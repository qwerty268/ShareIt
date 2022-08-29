package qwerty268.ShareIt.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qwerty268.ShareIt.exceptions.InvalidArgsException;

import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO) {
        validateUser(userDTO);
        return userClient.add(userDTO);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDTO userDTO, @PathVariable @Min(0) Long id) {
        validateUser(userDTO);
        return userClient.update(userDTO, id);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        return userClient.getAll();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable @Min(0) Long id) {
        userClient.deleteById(id);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable @Min(0) Long id) {
        return userClient.getById(id);
    }

    private void validateUser(UserDTO userDTO) {
        if (!EmailValidator.getInstance().isValid(userDTO.getEmail())) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }
}
