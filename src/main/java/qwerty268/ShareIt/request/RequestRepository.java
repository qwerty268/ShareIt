package qwerty268.ShareIt.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findRequestsByRequestorId(Long requestorId);

    List<Request> findRequestsByRequestorIdNot(Long userId, Pageable pageable);
}
