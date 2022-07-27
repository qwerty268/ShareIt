package qwerty268.ShareIt.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerId(Long bookerId);

    List<Booking> findBookingByStartAfterAndBookerId(Timestamp timestamp, Long bookerId);

    List<Booking> findBookingsByStartBeforeAndEndAfterAndBookerId(Timestamp timestamp1, Timestamp timestamp2,
                                                                  Long bookerId);

    @Query(value = "SELECT * FROM Bookings AS b" +
            " WHERE b.status = ?1 and b.booker_id = ?2",
            nativeQuery = true)
    List<Booking> findBookingsByStatusEqualsIgnoreCaseAndBookerId(Status status, Long bookerId);

    List<Booking> findBookingsByItemIdIn(Collection<Long> ids);

    List<Booking> findBookingsByItemIdInAndStartAfter(Collection<Long> ids, Timestamp timestamp);

    List<Booking> findBookingsByItemIdInAndStartBeforeAndEndAfter(Collection<Long> ids, Timestamp timestamp1,
                                                                  Timestamp timestamp2);

    List<Booking> findBookingsByItemIdInAndEndBefore(Collection<Long> ids, Timestamp timestamp);


    @Query(value = "SELECT * FROM Bookings AS b" +
            " WHERE b.status = ?1 and b.item_id in (SELECT i.id FROM Items AS i WHERE i.owner_id = ?2)",
            nativeQuery = true)
    List<Booking> findBookingsByStatusAndOwnerId(String state, Long ownerId);


    @Query(value = "SELECT * FROM Bookings AS b" +
            " WHERE b.status = 'APPROVED' and b.item_id in (SELECT i.id FROM Items AS i WHERE i.owner_id = ?1)" +
            " and b.start_date < CURRENT_TIMESTAMP AND b.end_date > CURRENT_TIMESTAMP AND item_id = ?2",
            nativeQuery = true)
    Booking findApprovedBookingForOwnerByItemId(Long ownerId, Long itemId);

    List<Booking> findBookingsByItemIdAndBookerIdAndStatusOrderByEndDesc(Long itemId, Long bookerId, Status status);

    @Query(value = "SELECT * FROM Bookings AS b" +
            " WHERE b.status = 'APPROVED' and b.item_id in (SELECT i.id FROM Items AS i WHERE i.owner_id = ?1)" +
            " and b.start_date > CURRENT_TIMESTAMP AND b.end_date > CURRENT_TIMESTAMP AND item_id = ?2" +
            " ORDER BY b.start_date",
            nativeQuery = true)
    List<Booking> findNextBookingForOwner(Long ownerId, Long itemId);


    @Query(value = "SELECT * FROM Bookings AS b" +
            " WHERE b.status = 'APPROVED' and b.item_id in (SELECT i.id FROM Items AS i WHERE i.owner_id = ?1)" +
            " and b.start_date <= CURRENT_TIMESTAMP AND item_id = ?2" +
            " ORDER BY b.end_date DESC",
            nativeQuery = true)
    List<Booking> findLastBookingForOwner(Long ownerId, Long itemId);
}
