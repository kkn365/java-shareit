package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemResponseMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserResponseMapper;
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
    public ItemResponseDto createItem(Long userId, ItemCreateDto item) {
        final User owner = UserResponseMapper.toUser(userService.getUser(userId));
        final Item newItem = Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(owner)
                .build();
        final Item addedItem = itemStorage.addNewItem(newItem);
        if (addedItem == null) {
            final String errorMessage = String.format("Couldn't add an item: %s", newItem);
            log.error(errorMessage);
            throw new InternalServerException(errorMessage);
        }
        return ItemResponseMapper.toItemResponseDto(addedItem);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, ItemUpdateDto itemData) {
        final User owner = UserResponseMapper.toUser(userService.getUser(userId));
        final Item currentItem = ItemResponseMapper.toItem(getItem(userId, itemId));
        final String incomingItemName = itemData.getName();
        final String currentItemName = currentItem.getName();
        final String incomingItemDesc = itemData.getDescription();
        final String currentItemDesc = currentItem.getDescription();
        final Item updatedItem = Item.builder()
                .id(itemId)
                .name(incomingItemName == null || incomingItemName.isEmpty() ? currentItemName : incomingItemName)
                .description(incomingItemDesc == null || incomingItemDesc.isEmpty() ? currentItemDesc : incomingItemDesc)
                .available(itemData.getAvailable() == null ? currentItem.getAvailable()
                        : itemData.getAvailable())
                .owner(owner)
                .build();
        itemStorage.updateItem(updatedItem);
        return getItem(userId, itemId);
    }

    @Override
    public ItemResponseDto getItem(Long ownerId, Long itemId) {
        userService.getUser(ownerId);
        final Optional<Item> currentItem = itemStorage.getItemById(itemId);
        if (currentItem.isEmpty()) {
            final String errorMessage = String.format("The item with id=%d not fount in the database.", itemId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return ItemResponseMapper.toItemResponseDto(currentItem.get());
    }

    @Override
    public Collection<ItemResponseDto> getAllItemsFromUser(Long userId) {
        return itemStorage.getAllItems().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemResponseMapper::toItemResponseDto)
                .toList();
    }

    @Override
    public Collection<ItemResponseDto> getAllItemsWithSearch(String text) {
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
                .map(ItemResponseMapper::toItemResponseDto)
                .toList();
    }
}
