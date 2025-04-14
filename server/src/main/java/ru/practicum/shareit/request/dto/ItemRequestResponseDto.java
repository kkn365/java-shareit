package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.dto.ItemRequestDtoResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(exclude = "items")
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private UserResponseDto requestor;
    private LocalDateTime created;
    private Collection<ItemRequestDtoResponseDto> items;
}
