package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service("itemService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Optional<ItemDto> createItem(Long userId, ItemDto item) {
        final User owner = UserMapper.toUser(userService.getUser(userId).get());
        final Item newItem = Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(owner)
                .build();
        final Optional<Item> addedItem = itemStorage.addNewItem(newItem);
        if (addedItem.isEmpty()) {
            final String errorMessage = String.format("Couldn't add an item: %s", newItem);
            log.error(errorMessage);
            throw new InternalServerException(errorMessage);
        }
        return Optional.of(ItemMapper.toItemDto(addedItem.get()));
    }

    @Override
    public Optional<ItemDto> updateItem(Long userId, Long itemId, ItemDto itemData) {
        final User owner = UserMapper.toUser(userService.getUser(userId).get());
        final Item currentItem = ItemMapper.toItem(getItem(userId, itemId).get());
        final Item updatedItem = Item.builder()
                .id(itemId)
                .name(itemData.getName() == null ? currentItem.getName() : itemData.getName())
                .description(itemData.getDescription() == null ? currentItem.getDescription() : itemData.getDescription())
                .available(itemData.getAvailable() == null ? currentItem.getAvailable() : itemData.getAvailable())
                .owner(owner)
                .build();
        itemStorage.updateItem(updatedItem);
        return getItem(userId, itemId);
    }

    @Override
    public Optional<ItemDto> getItem(Long ownerId, Long itemId) {
        userService.getUser(ownerId);
        final Optional<Item> currentItem = itemStorage.getItemById(itemId);
        if (currentItem.isEmpty()) {
            final String errorMessage = String.format("The item with id=%d not fount in the database.", itemId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return Optional.of(ItemMapper.toItemDto(currentItem.get()));
    }

    @Override
    public Collection<ItemDto> getAllItemsFromUser(Long userId) {
        return itemStorage.getAllItems().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getAllItemsWithSearch(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        final Pattern pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
        return itemStorage.getAllItems().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> {
                    if (pattern.matcher(item.getName()).find()) {
                        return true;
                    }
                    if (pattern.matcher(item.getDescription()).find()) {
                        return true;
                    }
                    return false;
                })
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
