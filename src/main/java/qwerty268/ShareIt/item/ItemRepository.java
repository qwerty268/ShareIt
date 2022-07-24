package qwerty268.ShareIt.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findItemById(Long itemId);

    List<Item> findItemsByOwnerId(Long ownerId);

    void deleteById(Long itemId);

    List<Item> findItemsByDescriptionIgnoreCaseAndNameIgnoreCaseAndIsAvailableTrue(String description, String name);

}
