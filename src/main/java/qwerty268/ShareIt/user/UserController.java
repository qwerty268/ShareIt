package qwerty268.ShareIt.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class UserController {

    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        repository.save(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        repository.update(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return repository.getAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return repository.getById(id)
                .orElseThrow();
    }
}
