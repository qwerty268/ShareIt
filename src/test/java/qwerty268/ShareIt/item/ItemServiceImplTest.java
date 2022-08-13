package qwerty268.ShareIt.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import qwerty268.ShareIt.booking.Booking;
import qwerty268.ShareIt.booking.BookingRepository;
import qwerty268.ShareIt.booking.Status;
import qwerty268.ShareIt.comment.CommentDTO;
import qwerty268.ShareIt.comment.CommentRepository;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserDTO;
import qwerty268.ShareIt.user.UserMapper;
import qwerty268.ShareIt.user.UserRepository;

import javax.swing.text.html.parser.Entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    ItemService itemService;

    @MockBean
    ItemRepository itemRepository;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    UserRepository userRepository;

    Item item = new Item(1L, "Дрель", "Очень крутая дрель", true, null, 1L);
    User user = new User(1L, "Иван Дрель", "mail@mail.ru");

    LocalDate localDateEnd = LocalDate.of(2022, 6, 20);
    Date date = Date.from(localDateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

    CommentDTO commentDTO = new CommentDTO(null, "comment",null, null, null, null);
    Booking booking = new Booking(1L, date,
            date, 1L, 1L, Status.APPROVED);

    @BeforeEach
    void createMockBehavior() {
        Mockito.when(itemRepository.save(any())).thenReturn(item);
        Mockito.when(itemRepository.findById(any())).thenReturn(Optional.ofNullable(item));
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findBookingsByItemIdAndBookerIdAndStatusOrderByEndDesc(any(), any(), any()))
                .thenReturn(List.of(booking));
    }

    @Test
    void save() {
        ItemDTO itemDTO = ItemMapper.toDTO(item);
        ItemDTO test = itemService.save(itemDTO, 1L);
        assertEquals(itemDTO, test);
    }

    @Test
    void update() {
        ItemDTO notUpdatedItem = new ItemDTO(1L, "Мега Дрель", "ну Очень крутая дрель", true,
                null, 1L);
        ItemDTO updatedItem = itemService.update(notUpdatedItem, 1L, 1L);
        assertEquals(notUpdatedItem, updatedItem);
    }

    @Test
    void addComment() {


    CommentDTO added = itemService.addComment(commentDTO, 1L, 1L);
    assertEquals(added.getItemId(), 1L);
    assertEquals(added.getAuthorId(), 1L);
    assertEquals(added.getItemId(), 1L);
    assertEquals(added.getText(),"comment");
    assertEquals(added.getAuthorName(), "Иван Дрель");
    assertTrue(added.getCreated() != null);
    }
}