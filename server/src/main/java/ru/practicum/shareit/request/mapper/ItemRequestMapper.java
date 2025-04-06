package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestResponseDto itemRequestResponseDto);
}
