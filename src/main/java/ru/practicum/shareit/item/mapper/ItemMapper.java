package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemResponseDto toItemDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemResponseDto itemResponseDto) {
        return Item.builder()
                .id(itemResponseDto.getId())
                .name(itemResponseDto.getName())
                .description(itemResponseDto.getDescription())
                .available(itemResponseDto.getAvailable())
                .build();
    }
}
