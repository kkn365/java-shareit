package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestMapperTest {

    @Autowired
    private ItemRequestMapper itemRequestMapper;

    @Test
    void toItemRequestResponseDto_whenInvokedWithItemRequest_thanReturnedItemRequestResponseDto() {
        final ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Test description")
                .requestor(null)
                .created(null)
                .build();

        ItemRequestResponseDto mappedRequestDto = itemRequestMapper.toItemRequestResponseDto(itemRequest);

        assertEquals(ItemRequestResponseDto.class, mappedRequestDto.getClass());
        assertEquals(itemRequest.getId(), mappedRequestDto.getId());
        assertEquals(itemRequest.getDescription(), mappedRequestDto.getDescription());
    }

    @Test
    void toItemRequest_whenInvokedWithItemRequestResponseDto_thanReturnedItemRequest() {
        final ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("Test description")
                .requestor(null)
                .created(null)
                .build();

        ItemRequest mappedItemRequest = itemRequestMapper.toItemRequest(itemRequestResponseDto);

        assertEquals(ItemRequest.class, mappedItemRequest.getClass());
        assertEquals(itemRequestResponseDto.getId(), mappedItemRequest.getId());
        assertEquals(itemRequestResponseDto.getDescription(), mappedItemRequest.getDescription());
    }
}