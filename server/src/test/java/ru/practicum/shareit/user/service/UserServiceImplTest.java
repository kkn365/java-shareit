package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserResponseMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserResponseMapper userResponseMapper;
    @Autowired
    private UserService userService;

    @AfterEach
    public void deleteUsers() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_whenInvokeWithCorrectUserCreateDto_thanReturnedCreatedUserResponseDto() {
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();

        UserResponseDto createdUserDto = userService.createUser(userCreateDto);
        User createdUser = userResponseMapper.toUser(createdUserDto);
        Optional<User> currentUser = userRepository.findById(createdUser.getId());

        assertTrue(currentUser.isPresent());
        assertEquals(currentUser.get(), createdUser);
    }

    @Test
    void createUser_whenInvokeWithUserWithSameEmail_thenDataAlreadyExistExceptionThrows() {
        User user = User.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name("Артем Столетов")
                .email("sfarada@bk.ru")
                .build();
        userRepository.save(user);

        assertThrows(DataAlreadyExistException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    void getUser_whenInvokedWithCorrectId_thenReturnedUserResponseDto() {
        User user = User.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();
        User savedUser = userRepository.save(user);

        UserResponseDto currentUserResponseDto = userService.getUser(savedUser.getId());
        UserResponseDto savedUserResponseDto = userResponseMapper.toUserResponseDto(savedUser);

        assertEquals(currentUserResponseDto, savedUserResponseDto);
    }

    @Test
    void getUser_whenInvokedWithIncorrectId_thenNotFoundExceptionThrows() {
        User user = User.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        userRepository.delete(savedUser);

        assertThrows(NotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void updateUser_whenInvokedWithCorrectData_thenReturnedUpdatedUserResponseDto() {
        User user = User.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name("Иван Фарада")
                .email("ifarada@bk.ru")
                .build();

        UserResponseDto updatedUserResponseDto = userService.updateUser(userId, userUpdateDto);

        assertEquals(updatedUserResponseDto.getId(), userId);
        assertEquals(updatedUserResponseDto.getName(), userUpdateDto.getName());
        assertEquals(updatedUserResponseDto.getEmail(), userUpdateDto.getEmail());
    }

    @Test
    void updateUser_whenInvokedWithExistedEmail_thenDataAlreadyExistExceptionThrows() {
        User user1 = User.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();
        User user2 = User.builder()
                .name("Иван Фарада")
                .email("ifarada@bk.ru")
                .build();
        Long userId = userRepository.save(user1).getId();
        userRepository.save(user2);
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name("Семен Фарада")
                .email("ifarada@bk.ru")
                .build();

        assertThrows(DataAlreadyExistException.class, () -> userService.updateUser(userId, userUpdateDto));
    }

    @Test
    void deleteUser_whenInvoked_thanUserDeleted() {
        User user = User.builder()
                .name("Семен Фарада")
                .email("sfarada@bk.ru")
                .build();
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        userService.deleteUser(userId);

        assertTrue(userRepository.findById(userId).isEmpty());
    }
}