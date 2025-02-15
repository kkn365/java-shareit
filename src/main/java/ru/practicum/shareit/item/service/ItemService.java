package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Optional<ItemDto> createItem(Long userId, @Valid ItemDto item);

    Optional<ItemDto> updateItem(Long userId, Long itemId, ItemDto item);

    Optional<ItemDto> getItem(Long userId, Long itemId);

    Collection<ItemDto> getAllItemsFromUser(Long userId);

    Collection<ItemDto> getAllItemsWithSearch(String text);
}
