package qwerty268.ShareIt.item;

import org.springframework.beans.factory.annotation.Autowired;
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
        validate(item, userId);

        item = itemRepository.save(item);
        return ItemMapper.toDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO update(ItemDTO itemDTO, Long userId, Long itemId) {
        Item item = ItemMapper.fromDTO(itemDTO, userId);
        Item notUpdatedItem = itemRepository.findById(itemId).orElseThrow(InvalidOwnerOfItemException::new);
        Item updatedItem = ItemMapper.update(notUpdatedItem, item);

        validate(item, userId);

        itemRepository.save(updatedItem);
        return ItemMapper.toDTO(updatedItem);
    }

    @Override
    public List<ItemWithBookingDatesAndCommentsDTO> findAll(Long userId) {

        List<Item> items = itemRepository.findItemsByOwnerId(userId);

        List<ItemWithBookingDatesAndCommentsDTO> itemWithBookingDatesDTOS = new ArrayList<>();

        items.forEach(item -> {
            itemWithBookingDatesDTOS.add(ItemMapper.createDTOFromItemBookingComments(item,
                    bookingRepository.findApprovedBookingForOwnerByItemId(item.getOwnerId(), item.getId()),
                    commentRepository.findCommentsByItemId(item.getId())));
        });


        return itemWithBookingDatesDTOS;
    }

    @Override
    public ItemWithBookingsAndCommentsDTO findById(Long itemId, Long bookerId) {
        Item item = itemRepository.findItemById(itemId).orElseThrow(ItemNotFoundException::new);

        return createDTO(item, bookerId);
    }

    @Override
    @Transactional
    public void deleteById(Long itemId, Long userId) {
        if (itemRepository.findItemById(itemId).get().getOwnerId() == userId) {

            itemRepository.deleteById(itemId);
        } else throw new InvalidArgsException();
    }

    @Override
    public List<ItemDTO> findItemsByParam(String text) {
        if (text.equals("")) {
            return List.of();
        }

        List<Item> items = itemRepository.search(text, text);

        List<ItemDTO> itemDTOS = new ArrayList<>();
        items.forEach(item -> itemDTOS.add(ItemMapper.toDTO(item)));

        return itemDTOS;
    }

    @Transactional
    @Override
    public CommentDTO addComment(CommentDTO commentDTO, Long userId) {
        Comment comment = CommentMapper.fromDTO(commentDTO);
        Booking booking = bookingRepository.findBookingsByItemIdAndBookerIdAndStatus(comment.getItemId(), userId,
                Status.APPROVED);

        if (booking.getEnd().before(Date.from(Instant.now()))) {
            comment = commentRepository.save(comment);
        }


        return CommentMapper.toDTO(comment);
    }

    private void validate(Item item, Long userId) {
        userRepository.findById(userId).orElseThrow(InvalidOwnerOfItemException::new);

        if (item.getName() == null ||
                item.getName().isBlank() ||
                item.getDescription() == null ||
                item.getIsAvailable() == null) {
            throw new InvalidArgsException();
        }
        if (!Objects.equals(item.getOwnerId(), userId)) {
            throw new ItemNotFoundException();
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
        return ItemMapper.createDTOFromItemBookingsComments(item, lastBooking, futureBooking,
                commentRepository.findCommentsByItemId(item.getId()));
    }
}
