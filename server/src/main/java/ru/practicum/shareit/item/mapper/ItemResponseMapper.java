package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemRequestDtoResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseShortDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemResponseMapper {

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemResponseDto toItemResponseDto(Item item);

    Item toItem(ItemResponseDto itemResponseDto);

    @Mapping(target = "ownerId", source = "owner.id")
    ItemRequestDtoResponseDto toItemRequestDtoResponseDto(Item item);

    ItemResponseShortDto toItemResponseShortDto(Item item);
}
