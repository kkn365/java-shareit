package ru.practicum.shareit.item.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository("itemRepository")
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new LinkedHashMap<>();
    private long generatorId = 0;

    @Override
    public Optional<Item> addNewItem(Item item) {
        final Long itemId = ++generatorId;
        final Item newItem = Item.builder()
                .id(itemId)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
        items.put(itemId, newItem);
        log.info("A new item has been added: {}.", newItem);
        return getItemById(itemId);
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        final Item item = items.get(itemId);
        if (item == null) {
            return Optional.empty();
        }
        return Optional.of(item);
    }

    @Override
    public Collection<Item> getAllItems() {
        return items.values();
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Item data has been updated: {}.", item);
    }
}
