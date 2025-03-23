package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    ItemResponseDto createItem(Long userId, ItemCreateDto item);

    ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto item);

    ItemResponseDto getItem(Long userId, Long itemId);

    Collection<ItemResponseDto> getAllItemsFromUser(Long userId);

    Collection<ItemResponseDto> getAllItemsWithSearch(String text);

    CommentResponseDto createComment(Long userId, Long itemId, CommentCreateDto commentCreateDto);
}
