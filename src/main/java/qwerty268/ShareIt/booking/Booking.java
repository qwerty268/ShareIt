package qwerty268.ShareIt.booking;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Table(name = "Bookings")
@Entity
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private Timestamp start;
    @Column(name = "end_date")
    private Timestamp end;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "booker_id")
    private Long bookerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking(Long id, Timestamp start, Timestamp end, Long itemId, Long bookerId, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }

    public Booking(Long id, Timestamp start, Timestamp end) {
        this.itemId = id;
        this.start = start;
        this.end = end;
    }
}

