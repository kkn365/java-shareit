package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void addUser() {
        userRepository.save(User.builder()
                .name("TestUserName")
                .email("testuseremail@mail.ru")
                .build()
        );
    }

    @Test
    void findByEmail_whenInvoked_thenReturnUser() {
        final String expectedEmail = "testuseremail@mail.ru";

        Optional<User> actualUser = userRepository.findByEmail(expectedEmail);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedEmail, actualUser.get().getEmail());
    }

    @AfterEach
    public void deleteUser() {
        userRepository.delete(User.builder()
                .name("TestUserName")
                .email("testuseremail@mail.ru")
                .build());
    }
}