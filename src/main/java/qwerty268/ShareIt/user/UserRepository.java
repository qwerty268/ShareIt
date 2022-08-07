package qwerty268.ShareIt.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "update Users set " +
            "name = ?2, " +
            "email = ?3 " +
            "where id = ?1", nativeQuery = true)
    void updateUser(Long userId, String name, String email);


}
