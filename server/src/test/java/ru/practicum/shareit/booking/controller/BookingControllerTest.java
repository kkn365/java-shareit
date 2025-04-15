package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void createBook_whenInvoked_thenResponseStatusCreatedWithBookingResponseDtoInBody() {
        long bookingId = 1L;
        long userId = 1L;
        long itemId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1L);
        ItemResponseShortDto expectedItemResponseShortDto = ItemResponseShortDto.builder()
                .id(itemId)
                .name("Бетононасос")
                .description("Неисправный бетононанос для красоты.")
                .build();
        UserResponseDto expectedUserResponseDto = UserResponseDto.builder()
                .id(userId)
                .name("Name")
                .email("email@mail.ru")
                .build();
        BookingResponseDto expectedBookingResponseDto = BookingResponseDto.builder()
                .id(bookingId)
                .booker(expectedUserResponseDto)
                .status(BookingStatus.WAITING)
                .item(expectedItemResponseShortDto)
                .start(start)
                .end(end)
                .build();
        BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(itemId)
                .start(start)
                .end(end)
                .build();
        Mockito.when(bookingService.createBook(userId, bookingCreateDto)).thenReturn(expectedBookingResponseDto);

        ResponseEntity<BookingResponseDto> response = bookingController.createBook(userId, bookingCreateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedBookingResponseDto, response.getBody());
    }

    @Test
    void updateBookingStatus_whenInvoked_thenResponseStatusOkWithBookingResponseDtoInBody() {
        long bookingId = 1L;
        long userId = 1L;
        BookingResponseDto expectedBookingResponseDto = BookingResponseDto.builder()
                .status(BookingStatus.APPROVED)
                .build();
        Mockito.when(bookingService.updateBookingStatus(userId, bookingId, true))
                .thenReturn(expectedBookingResponseDto);

        ResponseEntity<BookingResponseDto> response = bookingController.updateBookingStatus(userId, bookingId, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingResponseDto, response.getBody());
    }

    @Test
    void getBookById_whenInvoked_thenResponseStatusOkWithBookingResponseDtoInBody() {
        long bookingId = 1L;
        long userId = 1L;
        BookingResponseDto expectedBookingResponseDto = BookingResponseDto.builder()
                .status(BookingStatus.REJECTED)
                .build();
        Mockito.when(bookingService.getBookingById(userId, bookingId))
                .thenReturn(expectedBookingResponseDto);

        ResponseEntity<BookingResponseDto> response = bookingController.getBookById(userId, bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookingResponseDto, response.getBody());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenResponseStatusOkWithEmptyListInBody() {
        long userId = 1L;
        Mockito.when(bookingService.getItemsBookingsListForCurrentUser(userId, BookingState.ALL))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Collection<BookingResponseDto>> response = bookingController
                .getItemsBookingsListForCurrentUser(userId, BookingState.ALL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvoked_thenResponseStatusOkWithEmptyListInBody() {
        long userId = 1L;
        Mockito.when(bookingService.getBookingsListForCurrentUser(userId, BookingState.ALL))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Collection<BookingResponseDto>> response = bookingController
                .getBookingsListForCurrentUser(userId, BookingState.ALL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }
}