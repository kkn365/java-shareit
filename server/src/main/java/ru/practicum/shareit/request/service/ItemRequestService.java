package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestResponseDto createItemRequest(Long userId, ItemRequestCreateDto requestCreateDto);

    Collection<ItemRequestResponseDto> getOwnItemRequests(Long userId);

    Collection<ItemRequestResponseDto> getAllRequests(Long userId);

    ItemRequestResponseDto getRequestById(Long userId, Long requestId);
}
