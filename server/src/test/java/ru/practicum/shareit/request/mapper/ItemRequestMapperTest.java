package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemRequestDtoResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestMapperTest {

    @Autowired
    private ItemRequestMapper itemRequestMapper;

    @Test
    void toItemRequestResponseDto_whenInvokedWithItemRequest_thanReturnedItemRequestResponseDto() {
        final User requestor = User.builder()
                .id(1L)
                .name("Test name")
                .email("requestor@mail.ru")
                .build();
        final Item item1 = Item.builder()
                .id(1L)
                .name("Test item 1")
                .description("Test description")
                .build();
        final ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test description")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .items(List.of(item1))
                .build();

        ItemRequestResponseDto mappedRequestDto = itemRequestMapper.toItemRequestResponseDto(itemRequest);

        assertEquals(ItemRequestResponseDto.class, mappedRequestDto.getClass());
        assertEquals(itemRequest.getId(), mappedRequestDto.getId());
        assertEquals(itemRequest.getDescription(), mappedRequestDto.getDescription());
    }

    @Test
    void toItemRequest_whenInvokedWithItemRequestResponseDto_thanReturnedItemRequest() {
        final UserResponseDto requestor = UserResponseDto.builder()
                .id(1L)
                .name("Test name")
                .email("email@mail.com")
                .build();
        final ItemRequestDtoResponseDto itemRequestDtoResponseDto = ItemRequestDtoResponseDto.builder()
                .id(1L)
                .name("Test item name")
                .ownerId(23L)
                .build();
        final ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("Test description")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .items(List.of(itemRequestDtoResponseDto))
                .build();

        ItemRequest mappedItemRequest = itemRequestMapper.toItemRequest(itemRequestResponseDto);

        assertEquals(ItemRequest.class, mappedItemRequest.getClass());
        assertEquals(itemRequestResponseDto.getId(), mappedItemRequest.getId());
        assertEquals(itemRequestResponseDto.getDescription(), mappedItemRequest.getDescription());
    }
}