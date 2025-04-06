package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
public class BookingCreateDto {
    @NotNull
    private Long itemId;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    @AssertTrue
    boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }
}
