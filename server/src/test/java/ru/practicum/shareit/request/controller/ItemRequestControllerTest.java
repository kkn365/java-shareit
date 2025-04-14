package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    final String description = "Item test description";
    final long userId = 1L;
    final long requestId = 1L;
    final LocalDateTime now = LocalDateTime.now();
    final ItemRequestCreateDto createDto = new ItemRequestCreateDto(description);
    final UserResponseDto userDto = UserResponseDto.builder()
            .id(userId)
            .name("Name")
            .email("email@mail.ru")
            .build();
    final ItemRequestResponseDto expectedRequestDto = ItemRequestResponseDto.builder()
            .id(requestId)
            .description(createDto.getDescription())
            .requestor(userDto)
            .created(now)
            .items(null)
            .build();

    @Test
    void createItemRequest_whenInvoked_thenResponseStatusCreatedWithItemRequestResponseDtoInBody() {
        Mockito.when(itemRequestService.createItemRequest(userId, createDto)).thenReturn(expectedRequestDto);

        ResponseEntity<ItemRequestResponseDto> response = itemRequestController.createItemRequest(userId, createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedRequestDto, response.getBody());
    }

    @Test
    void getOwnItemRequests_whenInvoked_thenResponseStatusOKWithCollectionOfItemRequestResponseDtoInBody() {
        Mockito.when(itemRequestService.getOwnItemRequests(userId)).thenReturn(List.of(expectedRequestDto));

        ResponseEntity<Collection<ItemRequestResponseDto>> response = itemRequestController.getOwnItemRequests(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(expectedRequestDto), response.getBody());
    }

    @Test
    void getAllItemRequests_whenInvoked_thenResponseStatusOKWithCollectionOfItemRequestResponseDtoInBody() {
        Mockito.when(itemRequestService.getAllRequests(userId)).thenReturn(List.of(expectedRequestDto));

        ResponseEntity<Collection<ItemRequestResponseDto>> response = itemRequestController.getAllItemRequests(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(expectedRequestDto), response.getBody());
    }

    @Test
    void getItemRequest_whenInvoked_thenResponseStatusOKWithItemRequestResponseDtoInBody() {
        Mockito.when(itemRequestService.getRequestById(userId, requestId)).thenReturn(expectedRequestDto);

        ResponseEntity<ItemRequestResponseDto> response = itemRequestController.getItemRequest(userId, requestId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedRequestDto, response.getBody());
    }
}