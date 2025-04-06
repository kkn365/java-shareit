package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemCreateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
