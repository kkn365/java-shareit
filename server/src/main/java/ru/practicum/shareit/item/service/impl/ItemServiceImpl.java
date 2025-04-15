package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentResponseMapper;
import ru.practicum.shareit.item.mapper.ItemResponseMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserResponseMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service("itemService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentResponseMapper commentResponseMapper;
    private final ItemResponseMapper itemResponseMapper;
    private final UserResponseMapper userResponseMapper;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemResponseDto createItem(Long userId, ItemCreateDto item) {
        final User owner = userResponseMapper.toUser(userService.getUser(userId));
        final Item newItem = Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(owner)
                .build();
        if (item.getRequestId() != null) {
            newItem.setRequest(itemRequestRepository.findById(item.getRequestId()).orElse(null));
        }
        final Item addedItem = itemRepository.save(newItem);
        return itemResponseMapper.toItemResponseDto(addedItem);
    }

    @Transactional
    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemData) {
        final Optional<Item> currentItem = itemRepository.findById(itemId);
        if (currentItem.isEmpty()) {
            final String errorMessage = String.format("The item with id=%d not fount in the database.", itemId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        Item item = currentItem.get();
        if (!item.getOwner().getId().equals(userId)) {
            final String message = "You cannot update the data of items that do not belong to you.";
            log.warn(message);
            throw new ForbiddenException(message);
        }
        item.setName(itemData.getName() == null ? item.getName() : itemData.getName());
        item.setDescription(itemData.getDescription() == null ? item.getDescription() : itemData.getDescription());
        item.setAvailable(itemData.getAvailable() == null ? item.getAvailable() : itemData.getAvailable());
        final Item updatedItem = itemRepository.save(item);
        return itemResponseMapper.toItemResponseDto(updatedItem);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemResponseDto getItem(Long ownerId, Long itemId) {
        userService.getUser(ownerId);
        final Optional<Item> currentItem = itemRepository.findById(itemId);
        if (currentItem.isEmpty()) {
            final String errorMessage = String.format("The item with id=%d not fount in the database.", itemId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        Collection<Comment> comments = commentRepository.findAllByItemId(itemId);
        if (!comments.isEmpty()) {
            currentItem.get().setComments(comments.stream().toList());
        }
        return itemResponseMapper.toItemResponseDto(currentItem.get());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemResponseDto> getAllItemsFromUser(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings = bookingRepository.findByOwnerId(userId);
        return itemRepository.findByOwnerId(userId).stream()
                .map(itemResponseMapper::toItemResponseDto)
                .peek(item -> {
                            item.setLastBooking(bookings.stream()
                                    .filter(booking -> booking.getEnd().isBefore(now))
                                    .findFirst()
                                    .map(Booking::getEnd)
                                    .orElse(null)
                            );
                            item.setNextBooking(bookings.stream()
                                    .filter(booking -> booking.getStart().isAfter(now))
                                    .findFirst()
                                    .map(Booking::getStart)
                                    .orElse(null)
                            );
                        }
                )
                .toList();
    }

    @Override
    public Collection<ItemResponseDto> getAllItemsWithSearch(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.findAvailableWithSearchInFieldNameOrDescription(text).stream()
                .map(itemResponseMapper::toItemResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentResponseDto createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        final User user = userResponseMapper.toUser(userService.getUser(userId));
        Collection<Booking> bookings = bookingRepository.findAllByItemIdAndBookerId(itemId, userId);
        if (bookings.isEmpty() ||
            !bookings.stream().findFirst().get().getStatus().equals(BookingStatus.APPROVED) ||
            bookings.stream().findFirst().get().getEnd().isAfter(LocalDateTime.now())) {
            final String message = "You cannot leave a comment if you have not booked.";
            log.warn(message);
            throw new ValidationException(message);
        }
        final Comment comment = Comment.builder()
                .text(commentCreateDto.getText())
                .item(bookings.stream().findFirst().get().getItem())
                .author(user)
                .created(LocalDateTime.now())
                .build();
        Comment savedComment = commentRepository.save(comment);
        return commentResponseMapper.toCommentResponseDto(savedComment);
    }
}
