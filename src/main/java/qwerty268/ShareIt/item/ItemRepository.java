package qwerty268.ShareIt.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByOwnerId(Long ownerId, Pageable pageable);

    void deleteById(Long itemId);

    @Query(value = "SELECT * FROM Items AS I" +
            " WHERE (upper(i.description) LIKE CONCAT('%',upper(?1),'%') OR" +
            " upper(i.name) LIKE CONCAT('%',upper(?2),'%'))" +
            " AND i.is_available",
            nativeQuery = true)
    List<Item> search(String description, String name, Pageable pageable);

    List<ItemShort> findAllByOwnerId(Long ownerId);

    List<Item> findItemsByRequestId(Long requestId);
}
