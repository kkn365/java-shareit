package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @Test
    void getUser_whenInvoked_thenResponseStatusOkWithUserResponseDtoInBody() {
        long userId = 1L;
        UserResponseDto expectedUserResponseDto = UserResponseDto.builder()
                .id(userId)
                .name("Name")
                .email("email@mail.ru")
                .build();
        Mockito.when(userService.getUser(userId)).thenReturn(expectedUserResponseDto);

        ResponseEntity<UserResponseDto> response = userController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserResponseDto, response.getBody());
    }

    @Test
    void createUser_whenInvoked_thenResponseStatusCreatedWithUserResponseDtoInBody() {
        long userId = 1L;
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name("Name")
                .email("email@mail.ru")
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .name("Name")
                .email("email@mail.ru")
                .build();
        Mockito.when(userService.createUser(userCreateDto)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.createUser(userCreateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
    }

    @Test
    void updateUser_whenInvoked_thenResponseStatusOkWithUserResponseDtoInBody() {
        long userId = 1L;
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .name("Name")
                .email("email@mail.ru")
                .build();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .name("Name")
                .email("email@mail.ru")
                .build();
        Mockito.when(userService.updateUser(userId, userUpdateDto)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.updateUser(userId, userUpdateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
    }

    @Test
    void deleteUser_whenInvoked_thenResponseStatusNoContentWithEmptyBody() {
        long userId = 1L;

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}