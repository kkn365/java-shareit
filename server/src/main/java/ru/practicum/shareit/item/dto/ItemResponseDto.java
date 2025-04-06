package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@Builder
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Collection<CommentResponseDto> comments;
    private ItemRequestDtoResponseDto request;
}
