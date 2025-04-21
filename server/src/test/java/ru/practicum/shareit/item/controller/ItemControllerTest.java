package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private final User owner = User.builder()
            .id(1L)
            .name("Test user")
            .email("email@mail.ru")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("Test item")
            .description("Test item description")
            .owner(owner)
            .available(true)
            .build();

    @Test
    void createItem_whenInvoked_thenResponseStatusCreatedWithItemResponseDtoInBody() {
        ItemCreateDto itemCreateDto = ItemCreateDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        ItemResponseDto expectedDto = ItemResponseDto.builder()
                .id(1L)
                .name(itemCreateDto.getName())
                .description(itemCreateDto.getDescription())
                .available(itemCreateDto.getAvailable())
                .build();
        Mockito.when(itemService.createItem(owner.getId(), itemCreateDto))
                .thenReturn(expectedDto);

        ResponseEntity<ItemResponseDto> response = itemController.createItem(owner.getId(), itemCreateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void updateItem_whenInvoked_thenResponseStatusOkWithItemResponseDtoInBody() {
        ItemUpdateDto itemUpdateDto = ItemUpdateDto.builder()
                .available(true)
                .build();
        ItemResponseDto expectedDto = ItemResponseDto.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        Mockito.when(itemService.updateItem(owner.getId(), item.getId(), itemUpdateDto))
                .thenReturn(expectedDto);

        ResponseEntity<ItemResponseDto> response = itemController.updateItem(owner.getId(), item.getId(), itemUpdateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void getItemById_whenInvoked_thenResponseStatusOkWithItemResponseDtoInBody() {
        ItemResponseDto expectedDto = ItemResponseDto.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        Mockito.when(itemService.getItem(owner.getId(), item.getId()))
                .thenReturn(expectedDto);

        ResponseEntity<ItemResponseDto> response = itemController.getItemById(owner.getId(), item.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void getAllItemsFromUser_whenInvoked_thenResponseStatusOkWithItemResponseDtoInBody() {
        ItemResponseDto expectedDto = ItemResponseDto.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        Mockito.when(itemService.getAllItemsFromUser(owner.getId()))
                .thenReturn(List.of(expectedDto));

        ResponseEntity<Collection<ItemResponseDto>> response = itemController.getAllItemsFromUser(owner.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(expectedDto), response.getBody());
    }

    @Test
    void searchItems_whenInvoked_thenResponseStatusOkWithItemResponseDtoInBody() {
        ItemResponseDto expectedDto = ItemResponseDto.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        Mockito.when(itemService.getAllItemsWithSearch(""))
                .thenReturn(List.of(expectedDto));

        ResponseEntity<Collection<ItemResponseDto>> response = itemController.searchItems("");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(expectedDto), response.getBody());
    }

    @Test
    void createComment_whenInvoked_thenResponseStatusCreatedWithCommentResponseDtoInBody() {
        CommentCreateDto commentCreateDto = CommentCreateDto.builder()
                .text("comment")
                .build();
        CommentResponseDto expectedComment = CommentResponseDto.builder()
                .id(0L)
                .itemId(item.getId())
                .authorName(owner.getName())
                .build();
        Mockito.when(itemService.createComment(owner.getId(), item.getId(), commentCreateDto))
                .thenReturn(expectedComment);

        ResponseEntity<CommentResponseDto> response = itemController.createComment(owner.getId(),
                item.getId(), commentCreateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedComment, response.getBody());
    }
}