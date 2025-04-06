package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class ItemUpdateDto {
    @Nullable
    private String name;
    @Nullable
    private String description;
    @Nullable
    private Boolean available;
}
