package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody @Valid ItemCreateDto item
    ) {
        log.info("Creating item {} by userId={}", item, userId);
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody @Valid ItemUpdateDto item
    ) {
        log.info("Updating itemId={} by userId={} with data {}", itemId, userId, item);
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId
    ) {
        log.info("Get itemId={} by userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsFromUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Get all items owned by userId={}", userId);
        return itemClient.getAllItemsFromUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Get all items contains text '{}'", text);
        return itemClient.getAllItemsWithSearch(text);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable @Valid Long itemId,
            @RequestBody @Valid CommentCreateDto commentDto
    ) {
        log.info("Creating comment '{}' for itemId={} by userId={}", commentDto, itemId, userId);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
