package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = {"available","owner", "lastBooking", "nextBooking", "comments", "request"})
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserResponseDto owner;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Collection<CommentResponseDto> comments;
    private ItemRequestDtoResponseDto request;
}
