package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemRequestDtoResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
}
