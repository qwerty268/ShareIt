package qwerty268.ShareIt.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();

    @Override

    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void deleteById(Long userId) {
        users.remove(userId);
    }

    @Override
    public void update(User user) {
        if (users.get(user.getId()) != null) {
            users.put(user.getId(), user);
        }
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
