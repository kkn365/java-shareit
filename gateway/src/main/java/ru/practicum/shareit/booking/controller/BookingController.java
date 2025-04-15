package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static ru.practicum.shareit.util.Constants.DEFAULT_SEARCH_VALUE;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBook(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody @Valid BookingCreateDto book
    ) {
        log.info("Creating booking {}, userId={}", book, userId);
        return bookingClient.createBook(userId, book);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        log.info("Updating bookingId={} status to '{}' by userId={}", bookingId, approved, userId);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId
    ) {
        log.info("Get bookingId={} by userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getItemsBookingsListForCurrentUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_VALUE) BookingState state
    ) {
        log.info("Get bookings with state '{}' of items owned by userId={}", state, userId);
        return bookingClient.getItemsBookingsListForCurrentUser(userId, state);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsListForCurrentUser(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(required = false, defaultValue = DEFAULT_SEARCH_VALUE) BookingState state
    ) {
        log.info("Get bookings with state '{}' of items by userId={}", state, userId);
        return bookingClient.getBookingsListForCurrentUser(userId, state);
    }
}
