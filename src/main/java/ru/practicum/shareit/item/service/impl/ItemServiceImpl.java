package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingStorage;
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
import ru.practicum.shareit.item.repository.CommentStorage;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.ItemService;
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
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final UserService userService;

    @Override
    public ItemResponseDto createItem(Long userId, ItemCreateDto item) {
        final User owner = UserResponseMapper.toUser(userService.getUser(userId));
        final Item newItem = Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(owner)
                .build();
        final Item addedItem = itemStorage.save(newItem);
        return ItemResponseMapper.toItemResponseDto(addedItem);
    }

    @Transactional
    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemData) {
        final User owner = UserResponseMapper.toUser(userService.getUser(userId));
        final ItemResponseDto currentItem = getItem(userId, itemId);
        final String incomingItemName = itemData.getName();
        final String currentItemName = currentItem.getName();
        final String incomingItemDesc = itemData.getDescription();
        final String currentItemDesc = currentItem.getDescription();
        final Item updatedItem = Item.builder()
                .id(itemId)
                .name(incomingItemName == null || incomingItemName.isEmpty() ? currentItemName : incomingItemName)
                .description(incomingItemDesc == null || incomingItemDesc.isEmpty() ? currentItemDesc : incomingItemDesc)
                .available(itemData.getAvailable() == null ? currentItem.getAvailable()
                        : itemData.getAvailable())
                .owner(owner)
                .build();
        itemStorage.save(updatedItem);
        return ItemResponseMapper.toItemResponseDto(updatedItem);
    }

    @Override
    public ItemResponseDto getItem(Long ownerId, Long itemId) {
        userService.getUser(ownerId);
        final Optional<Item> currentItem = itemStorage.findById(itemId);
        if (currentItem.isEmpty()) {
            final String errorMessage = String.format("The item with id=%d not fount in the database.", itemId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return ItemResponseMapper.toItemResponseDto(currentItem.get());
    }

    @Override
    public Collection<ItemResponseDto> getAllItemsFromUser(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        Collection<Booking> bookings = bookingStorage.findByOwnerId(userId);
        return itemStorage.findByOwnerId(userId).stream()
                .map(ItemResponseMapper::toItemResponseDto)
                .peek(item -> {
                            item.setLastBooking(bookings.stream()
                                    .filter(booking -> {
                                        return booking.getEnd().isBefore(now);
                                    })
                                    .findFirst()
                                    .map(Booking::getEnd)
                                    .orElse(null)
                            );
                            item.setNextBooking(bookings.stream()
                                    .filter(booking -> {
                                        return booking.getStart().isAfter(now);
                                    })
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
        return itemStorage.findAvailableWithSearchInFieldNameOrDescription(text).stream()
                .map(ItemResponseMapper::toItemResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentResponseDto createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto) {
        final User user = UserResponseMapper.toUser(userService.getUser(userId));
        Collection<Booking> bookings = bookingStorage.findAllByItemIdAndBookerId(itemId, userId);
        if (bookings.isEmpty() || !bookings.stream().findFirst().get().getStatus().equals(BookingStatus.APPROVED) ||
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
        Comment savedComment = commentStorage.save(comment);
        return CommentResponseMapper.toCommentResponseDto(savedComment);
    }
}
