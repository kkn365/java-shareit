package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemService itemService;

    private User owner;
    private User requestor;
    private ItemRequest itemRequest;
    private ItemCreateDto itemCreateDto;

    @BeforeEach
    void init() {
        owner = userRepository.save(User.builder()
                .name("Геворг Мкртчан")
                .email("mkrtchan.g@mail.ru")
                .build());
        requestor = userRepository.save(User.builder()
                .name("Макеев Владимир")
                .email("makeev@ya.ru")
                .build());
        itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .description("Test item request description.")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build());
        itemCreateDto = ItemCreateDto.builder()
                .name("Test item")
                .description("Test item description")
                .available(false)
                .requestId(itemRequest.getId())
                .build();
    }

    @Test
    void createItem_whenInvoked_thenReturnedItemResponseDto() {
        ItemResponseDto createdItem = itemService.createItem(owner.getId(), itemCreateDto);

        assertEquals(itemCreateDto.getName(), createdItem.getName());
        assertEquals(itemCreateDto.getDescription(), createdItem.getDescription());
        assertEquals(itemCreateDto.getAvailable(), createdItem.getAvailable());
        assertEquals(itemCreateDto.getRequestId(), createdItem.getRequest().getId());
    }

    @Test
    void updateItem_whenInvokedWithIncorrectItemId_thenNotFoundExceptionThrows() {
        ItemResponseDto createdItem = itemService.createItem(owner.getId(), itemCreateDto);
        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .available(true)
                .build();

        assertThrows(NotFoundException.class, () ->
                itemService.updateItem(owner.getId(), createdItem.getId() + 1, updateDto));
    }

    @Test
    void updateItem_whenInvokedWithIncorrectUserId_thenForbiddenExceptionThrows() {
        ItemResponseDto createdItem = itemService.createItem(owner.getId(), itemCreateDto);
        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .available(true)
                .build();

        assertThrows(ForbiddenException.class, () ->
                itemService.updateItem(owner.getId() + 1000L, createdItem.getId(), updateDto));
    }

    @Test
    void updateItem_whenInvoked_thenReturnedUpdatedItemResponseDto() {
        ItemResponseDto createdItem = itemService.createItem(owner.getId(), itemCreateDto);
        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .name("Test item new name")
                .description("Test item new description")
                .available(true)
                .build();

        ItemResponseDto updatedItem = itemService.updateItem(owner.getId(), createdItem.getId(), updateDto);

        assertEquals(updateDto.getName(), updatedItem.getName());
        assertEquals(updateDto.getDescription(), updatedItem.getDescription());
        assertEquals(updateDto.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    void getItem_whenInvokedWithExistedItemId_thenReturnedItemResponseDto() {
        ItemResponseDto createdItem = itemService.createItem(owner.getId(), itemCreateDto);

        ItemResponseDto currentItem = itemService.getItem(owner.getId(), createdItem.getId());

        assertEquals(createdItem, currentItem);
    }

    @Test
    void getItem_whenInvokedWithIncorrectItemId_thenNotFoundExceptionThrows() {
        ItemResponseDto createdItem = itemService.createItem(owner.getId(), itemCreateDto);

        assertThrows(NotFoundException.class, () ->
                itemService.getItem(owner.getId(), createdItem.getId() + 1));
    }

    @Test
    void getAllItemsFromUser_whenInvoked_thenReturnedNextAndLastBookingsDates() {
        User booker = userRepository.save(User.builder()
                .name("Booker")
                .email("booker@mail.ru")
                .build());
        Item item = itemRepository.save(Item.builder()
                .name("Test item")
                .description("Test item description")
                .owner(owner)
                .available(true)
                .build()
        );
        Booking lastBooking = bookingRepository.save(Booking.builder()
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build()
        );
        Booking nextBooking = bookingRepository.save(Booking.builder()
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .build()
        );

        Collection<ItemResponseDto> items = itemService.getAllItemsFromUser(owner.getId());
        ItemResponseDto firstItem = items.stream().findFirst().orElse(null);

        assert firstItem != null;
        assertEquals(item.getName(), firstItem.getName());
        assertEquals(lastBooking.getEnd(), firstItem.getLastBooking());
        assertEquals(nextBooking.getStart(), firstItem.getNextBooking());
    }

    @Test
    void getAllItemsWithSearch_whenInvokedWithEmptyTextVariable_thenReturnedEmptyList() {
        final String text = "";

        Collection<ItemResponseDto> result = itemService.getAllItemsWithSearch(text);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getAllItemsWithSearch_whenInvoked_thenReturnedLostOfItemResponseDto() {
        final String text = "Test search string";
        itemRepository.save(Item.builder()
                .name(text)
                .description("Test item description")
                .owner(owner)
                .available(true)
                .build()
        );
        itemRepository.save(Item.builder()
                .name("Test item2")
                .description(text)
                .owner(owner)
                .available(true)
                .build()
        );

        Collection<ItemResponseDto> result = itemService.getAllItemsWithSearch(text);

        assertEquals(2, result.size());
    }


    @Test
    void createComment_whenInvokedWithIncorrectData_thenValidationExceptionThrows() {
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("Test comment.")
                .build();

        assertThrows(ValidationException.class, () ->
                itemService.createComment(owner.getId(), 0L, commentCreateDto));
    }

    @Test
    void createComment_whenInvoked_thenReturnedCommentResponseDto() {
        Item item = itemRepository.save(Item.builder()
                .name("Test item")
                .description("Test item description")
                .owner(owner)
                .available(true)
                .build()
        );
        User booker = userRepository.save(User.builder()
                .name("Booker")
                .email("booker@mail.ru")
                .build());
        bookingRepository.save(Booking.builder()
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build()
        );
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("Test comment.")
                .build();

        CommentResponseDto result = itemService.createComment(booker.getId(), item.getId(), commentCreateDto);

        assertEquals(CommentResponseDto.class, result.getClass());
        assertEquals(commentCreateDto.getText(), result.getText());
    }

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }
}