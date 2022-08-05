package qwerty268.ShareIt.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qwerty268.ShareIt.booking.Booking;
import qwerty268.ShareIt.booking.BookingRepository;
import qwerty268.ShareIt.booking.Status;
import qwerty268.ShareIt.comment.Comment;
import qwerty268.ShareIt.comment.CommentDTO;
import qwerty268.ShareIt.comment.CommentMapper;
import qwerty268.ShareIt.comment.CommentRepository;
import qwerty268.ShareIt.exception.InvalidArgsException;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.item.exceptions.ItemNotFoundException;
import qwerty268.ShareIt.user.UserRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ItemDTO save(ItemDTO itemDTO, Long userId) {
        Item item = ItemMapper.fromDTO(itemDTO, userId);
        item.setOwnerId(userId);
        validateItem(item, userId);

        item = itemRepository.save(item);
        log.info("Предмет сохранён");
        return ItemMapper.toDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO update(ItemDTO itemDTO, Long userId, Long itemId) {
        Item item = ItemMapper.fromDTO(itemDTO, userId);
        Item notUpdatedItem = itemRepository.findById(itemId).orElseThrow(InvalidOwnerOfItemException::new);
        Item updatedItem = ItemMapper.update(notUpdatedItem, item);

        validateItem(item, userId);

        itemRepository.save(updatedItem);
        log.info("Предмет обновлён");
        return ItemMapper.toDTO(updatedItem);
    }

    @Override
    public List<ItemWithBookingsAndCommentsDTO> findAll(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);

        List<Item> items = itemRepository.findItemsByOwnerId(userId, pageable);

        List<ItemWithBookingsAndCommentsDTO> itemWithBookingDatesDTOS = new ArrayList<>();

        items.forEach(item -> {
            itemWithBookingDatesDTOS.add(createDTO(item, userId));
        });

        log.info("Предметы возвращены");
        return itemWithBookingDatesDTOS;
    }

    @Override
    public ItemWithBookingsAndCommentsDTO findById(Long itemId, Long bookerId) {
        Item item = itemRepository.findItemById(itemId).orElseThrow(ItemNotFoundException::new);

        ItemWithBookingsAndCommentsDTO itemWithBookingsAndCommentsDTO = createDTO(item, bookerId);
        log.info("Предметы возвращены");
        return itemWithBookingsAndCommentsDTO;
    }

    @Override
    @Transactional
    public void deleteById(Long itemId, Long userId) {
        if (itemRepository.findItemById(itemId).get().getOwnerId() == userId) {

            itemRepository.deleteById(itemId);
            log.info("Предмет удалён");
        } else {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }

    @Override
    public List<ItemDTO> findItemsByParam(String text, int from, int size) {
        if (text.equals("")) {
            return List.of();
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Item> items = itemRepository.search(text, text, pageable);

        List<ItemDTO> itemDTOS = new ArrayList<>();
        items.forEach(item -> itemDTOS.add(ItemMapper.toDTO(item)));
        log.info("Предметы возвращены");

        return itemDTOS;
    }

    @Transactional
    @Override
    public CommentDTO addComment(CommentDTO commentDTO, Long userId, Long itemId) {
        Comment comment = CommentMapper.fromDTO(commentDTO);
        comment.setItemId(itemId);

        validateComment(comment, userId);

        List<Booking> bookings = bookingRepository
                .findBookingsByItemIdAndBookerIdAndStatusOrderByEndDesc(comment.getItemId(), userId, Status.APPROVED);

        if (bookings.size() == 0) {
            throw new InvalidArgsException();
        }
        Booking booking = bookings.get(0);


        if (booking.getStart().toInstant().isBefore(Instant.now())) {
            comment = commentRepository.save(comment);
        } else {
            throw new InvalidArgsException();
        }

        log.info("Комментарий добавлен");
        return CommentMapper.toDTO(comment, userRepository.findById(comment.getAuthorId()).get().getName());
    }

    private void validateItem(Item item, Long userId) {
        userRepository.findById(userId).orElseThrow(InvalidOwnerOfItemException::new);

        if (item.getName() == null ||
                item.getName().isBlank() ||
                item.getDescription() == null ||
                item.getIsAvailable() == null) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
        if (!Objects.equals(item.getOwnerId(), userId)) {
            log.error("ItemNotFoundException");
            throw new ItemNotFoundException();
        }
    }

    private void validateComment(Comment comment, Long userId) {
        userRepository.findById(userId).orElseThrow(InvalidArgsException::new);
        if (comment.getText() == null || Objects.equals(comment.getText(), "")) {
            log.error("InvalidArgsException");
            throw new InvalidArgsException();
        }
    }

    private ItemWithBookingsAndCommentsDTO createDTO(Item item, Long bookerId) {

        //находим ближайшее следующее
        Booking futureBooking = null;
        Booking lastBooking = null;

        if (bookerId == item.getOwnerId()) {
            Booking booking = bookingRepository.findApprovedBookingForOwnerByItemId(item.getOwnerId(), item.getId());
            List<Booking> bookings = bookingRepository.findNextBookingForOwner(item.getOwnerId(), item.getId());
            if (bookings.size() != 0) {
                lastBooking = bookings.get(0);
            }
            if (bookings.size() == 2) {
                futureBooking = bookings.get(1);
            }


            if (futureBooking == null) {
                futureBooking = booking;
            }

            if (lastBooking == null) {
                lastBooking = booking;
            }
        }
        List<CommentDTO> commentDTOS = new ArrayList<>();

        List<Comment> comments = commentRepository.findCommentsByItemId(item.getId());

        comments.forEach(comment -> commentDTOS.add(
                CommentMapper.toDTO(comment, userRepository.findById(comment.getAuthorId()).get().getName())
        ));

        return ItemMapper.createDTOFromItemBookingsComments(item, lastBooking, futureBooking,
                commentDTOS);
    }
}
