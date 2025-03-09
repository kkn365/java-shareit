package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Item addNewItem(Item item);

    Optional<Item> getItemById(Long itemId);

    Collection<Item> getAllItems();

    void updateItem(Item item);
}
