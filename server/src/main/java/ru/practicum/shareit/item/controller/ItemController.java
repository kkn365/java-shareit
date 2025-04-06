package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    ResponseEntity<ItemResponseDto> createItem(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody ItemCreateDto item
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.createItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    ItemResponseDto updateItem(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemUpdateDto item
    ) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ItemResponseDto getItemById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId
    ) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    Collection<ItemResponseDto> getAllItemsFromUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getAllItemsFromUser(userId);
    }

    @GetMapping("/search")
    Collection<ItemResponseDto> searchItems(@RequestParam String text) {
        return itemService.getAllItemsWithSearch(text);
    }

    @PostMapping("{itemId}/comment")
    ResponseEntity<CommentResponseDto> createComment(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody CommentCreateDto commentDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.createComment(userId, itemId, commentDto));
    }
}
