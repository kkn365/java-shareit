package ru.practicum.shareit.booking.model;

import lombok.Builder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;

    private enum BookingStatus {
        WAITING,  // новое бронирование, ожидает одобрения
        APPROVED, // бронирование подтверждено владельцем
        REJECTED, // бронирование отклонено владельцем
        CANCELED  // бронирование отменено создателем
    }
}
