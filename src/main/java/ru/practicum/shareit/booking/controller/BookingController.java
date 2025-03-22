package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    BookingResponseDto createBook(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody @Valid BookingCreateDto book
    ) {
        return bookingService.createBook(userId, book);
    }

    @PatchMapping("/{bookingId}")
    BookingResponseDto updateBookingStatus(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingResponseDto getBookById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping("/owner")
    Collection<BookingResponseDto> getItemsBookingsListForCurrentUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_VALUE) BookingState state
    ) {
        return bookingService.getItemsBookingsListForCurrentUser(userId, state);
    }

    @GetMapping()
    Collection<BookingResponseDto> getBookingsListForCurrentUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_VALUE) BookingState state
    ) {
        return bookingService.getBookingsListForCurrentUser(userId, state);
    }
}
