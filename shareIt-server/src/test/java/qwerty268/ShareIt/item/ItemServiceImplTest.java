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
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.item.exceptions.ItemNotFoundException;
import qwerty268.ShareIt.user.User;
import qwerty268.ShareIt.user.UserRepository;
import qwerty268.ShareIt.user.exceptions.UserNotFoundException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private UserRepository userRepository;

    private final Item item = new Item(1L, "Дрель", "Очень крутая дрель", true, null, 1L);
    private final User user = new User(1L, "Иван Дрель", "mail@mail.ru");

    private final Instant instant = Instant.now();

    private final Date date = Date.from(instant);

    private final CommentDTO commentDTO = new CommentDTO(null, "comment", null, null, null, null);
    private final Booking booking = new Booking(1L, date,
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
    void saveWithUnknownOwner() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        ItemDTO itemDTO = ItemMapper.toDTO(item);
        assertThrows(UserNotFoundException.class, () -> itemService.save(itemDTO, 1L));
    }

    @Test
    void saveWithInvalidArg() {
        ItemDTO itemDTO = ItemMapper.toDTO(item);
        itemDTO.setDescription("");

        assertThrows(InvalidArgsException.class, () -> itemService.save(itemDTO, 1L));
    }

    @Test
    void update() {
        ItemDTO notUpdatedItem = new ItemDTO(1L, "Мега Дрель", "ну Очень крутая дрель", true,
                null, 1L);
        ItemDTO updatedItem = itemService.update(notUpdatedItem, 1L, 1L);
        assertEquals(notUpdatedItem, updatedItem);
    }

    @Test
    void updateWithWrongItemId() {
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ItemDTO notUpdatedItem = new ItemDTO(1L, "Мега Дрель", "ну Очень крутая дрель", true,
                null, 1L);

        assertThrows(ItemNotFoundException.class, () -> itemService.update(notUpdatedItem, 1L, 1L));
    }

    @Test
    void updateWithAnotherOwner() {
        ItemDTO notUpdatedItem = new ItemDTO(1L, "Мега Дрель", "ну Очень крутая дрель", false,
                null, null);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));

        assertThrows(InvalidOwnerOfItemException.class, () -> itemService.update(notUpdatedItem, 2L, 1L));
    }

    @Test
    void addComment() {
        CommentDTO added = itemService.addComment(commentDTO, 1L, 1L);
        assertEquals(added.getItemId(), 1L);
        assertEquals(added.getAuthorId(), 1L);
        assertEquals(added.getItemId(), 1L);
        assertEquals(added.getText(), "comment");
        assertEquals(added.getAuthorName(), "Иван Дрель");
        assertNotNull(added.getCreated());
    }

    @Test
    void addWithUnknownUser() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> itemService.addComment(commentDTO, 2L, 1L));
    }

    @Test
    void addWithBlankText() {
        commentDTO.setText("");
        assertThrows(InvalidArgsException.class, () -> itemService.addComment(commentDTO, 2L, 1L));
    }
}