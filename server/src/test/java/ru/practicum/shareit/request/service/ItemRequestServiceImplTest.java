package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemRequestServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRequestService itemRequestService;

    @AfterEach
    public void deleteRequests() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void createItemRequest_whenInvoked_thenReturnedItemRequestResponseDto() {
        final ItemRequestCreateDto createDto = new ItemRequestCreateDto("Item test description");
        final UserResponseDto createdUser = userService.createUser(UserCreateDto.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build());

        final ItemRequestResponseDto expectedDto = itemRequestService.createItemRequest(createdUser.getId(), createDto);

        assertEquals(createDto.getDescription(), expectedDto.getDescription());
        assertEquals(createdUser.getName(), expectedDto.getRequestor().getName());
        assertEquals(createdUser.getEmail(), expectedDto.getRequestor().getEmail());
    }

    @Test
    void getOwnItemRequests_whenInvoked_thenReturnedListItemRequestResponseDto() {
        final ItemRequestCreateDto createDto1 = new ItemRequestCreateDto("Item test description 1");
        final ItemRequestCreateDto createDto2 = new ItemRequestCreateDto("Item test description 2");
        final ItemRequestCreateDto createDto3 = new ItemRequestCreateDto("Item test description 3");
        final UserResponseDto createdUser = userService.createUser(UserCreateDto.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build());
        itemRequestService.createItemRequest(createdUser.getId(), createDto1);
        itemRequestService.createItemRequest(createdUser.getId(), createDto2);
        itemRequestService.createItemRequest(createdUser.getId(), createDto3);

        Collection<ItemRequestResponseDto> requests = itemRequestService.getOwnItemRequests(createdUser.getId());

        assertEquals(createDto3.getDescription(), requests.stream().findFirst().get().getDescription());
    }

    @Test
    void getAllRequests_whenInvoked_thenReturnedAllItemRequestResponseDto() {
        final ItemRequestCreateDto createDto1 = new ItemRequestCreateDto("Item test description 1");
        final ItemRequestCreateDto createDto2 = new ItemRequestCreateDto("Item test description 2");
        final ItemRequestCreateDto createDto3 = new ItemRequestCreateDto("Item test description 3");
        final UserResponseDto createdUser = userService.createUser(UserCreateDto.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build());
        itemRequestService.createItemRequest(createdUser.getId(), createDto1);
        itemRequestService.createItemRequest(createdUser.getId(), createDto2);
        itemRequestService.createItemRequest(createdUser.getId(), createDto3);

        Collection<ItemRequestResponseDto> requests = itemRequestService.getAllRequests(createdUser.getId());

        assertEquals(3, requests.size());
    }

    @Test
    void getRequestById_whenInvoked_thenReturnedItemRequestResponseDto() {
        final ItemRequestCreateDto createDto = new ItemRequestCreateDto("Item test description");
        final UserResponseDto createdUser = userService.createUser(UserCreateDto.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build());
        final ItemRequestResponseDto createdRequest = itemRequestService.createItemRequest(createdUser.getId(),
                createDto);

        final ItemRequestResponseDto request = itemRequestService.getRequestById(createdUser.getId(),
                createdRequest.getId());

        assertEquals(createdRequest, request);
    }

    @Test
    void getRequestById_whenInvokedWithIncorrectRequestId_thenNotFoundExceptionThrows() {
        final ItemRequestCreateDto createDto = new ItemRequestCreateDto("Item test description");
        final UserResponseDto createdUser = userService.createUser(UserCreateDto.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build());
        final ItemRequestResponseDto createdRequest = itemRequestService.createItemRequest(createdUser.getId(), createDto);

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(createdUser.getId() - 1,
                createdRequest.getId()));
    }
}