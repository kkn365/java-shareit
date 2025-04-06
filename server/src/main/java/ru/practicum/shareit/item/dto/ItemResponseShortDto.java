package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ItemResponseShortDto {
    private Long id;
    private String name;
    private String description;
}
