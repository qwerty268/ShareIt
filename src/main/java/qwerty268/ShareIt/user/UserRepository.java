package qwerty268.ShareIt.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    void deleteById(Long userId);

    void update(User user);

    Optional<User> getById(Long userId);

    List<User> getAll();
}
