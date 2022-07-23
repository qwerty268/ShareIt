package qwerty268.ShareIt.booking;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "Bookings")
public class Booking {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private Date start;
    @Column(name = "end_date")
    private Date end;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "booker_id")
    private Long bookerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Booking(Long id, Date start, Date end, Long itemId, Long bookerId, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }
}

