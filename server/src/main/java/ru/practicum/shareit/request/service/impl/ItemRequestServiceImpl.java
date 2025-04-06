package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDtoResponseDto;
import ru.practicum.shareit.item.mapper.ItemResponseMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.mapper.UserResponseMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service("itemRequestService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRequestMapper itemRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final ItemResponseMapper itemResponseMapper;

    @Transactional
    @Override
    public ItemRequestResponseDto createItemRequest(Long userId, ItemRequestCreateDto requestCreateDto) {
        final User requestor = userResponseMapper.toUser(userService.getUser(userId));
        final ItemRequest request = ItemRequest.builder()
                .description(requestCreateDto.getDescription())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        final ItemRequest addedRequest = itemRequestRepository.save(request);
        return itemRequestMapper.toItemRequestResponseDto(addedRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemRequestResponseDto> getOwnItemRequests(Long userId) {
        userService.getUser(userId);
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(itemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemRequestResponseDto> getAllRequests(Long userId) {
        userService.getUser(userId);
        return itemRequestRepository.findAll()
                .stream()
                .map(itemRequestMapper::toItemRequestResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestResponseDto getRequestById(Long userId, Long requestId) {
        userService.getUser(userId);
        final ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request id=%d not found", requestId)));
        final Collection<ItemRequestDtoResponseDto> items = request.getItems().stream()
                .map(itemResponseMapper::toItemRequestDtoResponseDto)
                .toList();
        ItemRequestResponseDto itemRequest = itemRequestMapper.toItemRequestResponseDto(request);
        itemRequest.setItems(items);
        return itemRequest;
    }
}
