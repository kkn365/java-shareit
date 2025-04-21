package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    ResponseEntity<ItemRequestResponseDto> createItemRequest(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody ItemRequestCreateDto itemRequestCreateDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemRequestService.createItemRequest(userId, itemRequestCreateDto));
    }

    @GetMapping
    ResponseEntity<Collection<ItemRequestResponseDto>> getOwnItemRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemRequestService.getOwnItemRequests(userId));
    }

    @GetMapping("/all")
    ResponseEntity<Collection<ItemRequestResponseDto>> getAllItemRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemRequestService.getAllRequests(userId));
    }

    @GetMapping("/{requestId}")
    ResponseEntity<ItemRequestResponseDto> getItemRequest(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long requestId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemRequestService.getRequestById(userId, requestId));
    }
}
