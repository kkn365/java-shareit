package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.util.Constants.DEFAULT_SEARCH_VALUE;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    ResponseEntity<BookingResponseDto> createBook(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody BookingCreateDto book
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.createBook(userId, book));
    }

    @PatchMapping("/{bookingId}")
    ResponseEntity<BookingResponseDto> updateBookingStatus(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingService.updateBookingStatus(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<BookingResponseDto> getBookById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping("/owner")
    ResponseEntity<Collection<BookingResponseDto>> getItemsBookingsListForCurrentUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_VALUE) BookingState state
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingService.getItemsBookingsListForCurrentUser(userId, state));
    }

    @GetMapping
    ResponseEntity<Collection<BookingResponseDto>> getBookingsListForCurrentUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_VALUE) BookingState state
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookingService.getBookingsListForCurrentUser(userId, state));
    }
}
