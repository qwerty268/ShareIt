package qwerty268.ShareIt.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    User user = new User(1L, "dd", "sss");
    Item item = new Item(1L, "111", "2", true, null, 1L);

    @Test
    void search() {
        userRepository.save(user);
        itemRepository.save(item);

        List<Item> items =  itemRepository.search("1", Pageable.ofSize(1));
        assertEquals(items.get(0).getId(), item.getId());
    }
}