package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingResponseDto createBook(Long userId, BookingCreateDto book);

    BookingResponseDto updateBookingStatus(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getBookingById(Long userId, Long bookingId);

    Collection<BookingResponseDto> getItemsBookingsListForCurrentUser(Long userId, BookingState state);

    Collection<BookingResponseDto> getBookingsListForCurrentUser(Long userId, BookingState state);
}
