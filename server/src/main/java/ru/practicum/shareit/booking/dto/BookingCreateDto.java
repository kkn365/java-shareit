package ru.practicum.shareit.booking.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookingCreateDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
