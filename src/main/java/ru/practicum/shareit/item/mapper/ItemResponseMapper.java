package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserResponseMapper;

import java.util.Collections;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemResponseMapper {

    public static ItemResponseDto toItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(UserResponseMapper.toUserResponseDto(item.getOwner()))
                .comments(Optional.ofNullable(item.getComments())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(CommentResponseMapper::toCommentResponseDto)
                        .toList())
                .build();
    }

    public static Item toItem(ItemResponseDto itemResponseDto) {
        return Item.builder()
                .id(itemResponseDto.getId())
                .name(itemResponseDto.getName())
                .description(itemResponseDto.getDescription())
                .available(itemResponseDto.getAvailable())
                .owner(UserResponseMapper.toUser(itemResponseDto.getOwner()))
                .build();
    }
}
