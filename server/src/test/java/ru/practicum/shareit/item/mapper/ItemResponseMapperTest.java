package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemRequestDtoResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemResponseMapperTest {

    @Autowired
    private ItemResponseMapper itemResponseMapper;

    final Long ownerId = 1L;
    final Long itemId = 1L;
    final User owner = User.builder()
            .id(ownerId)
            .name("Test user")
            .email("email@mail.ru")
            .build();
    final Item item = Item.builder()
            .id(itemId)
            .name("Test item")
            .description("Test description")
            .owner(owner)
            .comments(List.of(Comment.builder().build()))
            .request(new ItemRequest())
            .build();
    final ItemResponseDto shortDto = ItemResponseDto.builder()
            .id(itemId)
            .name("Test item")
            .description("Test description")
            .build();

    @Test
    void toItemResponseDto() {
        ItemResponseDto result = itemResponseMapper.toItemResponseDto(item);

        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void toItem() {
        Item result = itemResponseMapper.toItem(shortDto);

        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void toItemRequestDtoResponseDto() {
        ItemRequestDtoResponseDto result = itemResponseMapper.toItemRequestDtoResponseDto(item);

        assertEquals(item.getName(), result.getName());
        assertEquals(item.getOwner().getId(), result.getOwnerId());
    }

    @Test
    void toItemResponseShortDto() {
        ItemResponseShortDto result = itemResponseMapper.toItemResponseShortDto(item);

        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }
}