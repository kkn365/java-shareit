package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class CommentResponseDto {
    private Long id;
    private String text;
    private Long itemId;
    private String authorName;
    private LocalDateTime created;
}
