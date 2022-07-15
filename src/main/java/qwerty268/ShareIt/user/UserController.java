package qwerty268.ShareIt.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public UserDTO addUser(@RequestBody UserDTO userDTO) {
        return service.add(userDTO);
    }

    @PatchMapping("/users/{id}")
    public UserDTO updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) {
        return service.update(userDTO, id);
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return service.getAll();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return service.getById(id);
    }

}
