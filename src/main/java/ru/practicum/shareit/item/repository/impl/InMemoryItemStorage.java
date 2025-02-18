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
    public Item addNewItem(Item item) {
        item.setId(++generatorId);
        items.put(item.getId(), item);
        log.info("A new item has been added: {}.", item);
        return item;
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
