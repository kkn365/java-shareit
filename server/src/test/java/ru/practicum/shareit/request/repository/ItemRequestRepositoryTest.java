package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequestorIdOrderByCreatedDesc() {
        final User user = userRepository.save(User.builder()
                .name("TestUserName")
                .email("testuseremail@mail.ru")
                .build()
        );
        itemRequestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .requestor(user)
                .description("Test item request description 1")
                .build());
        final  ItemRequest itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .requestor(user)
                .description("Test item request description 2")
                .build());

        Collection<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId());

        assertEquals(2, requests.size());
        assertEquals(itemRequest, requests.stream().findFirst().get());
    }
}