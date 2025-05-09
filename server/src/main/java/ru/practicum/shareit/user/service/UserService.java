package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserResponseDto createUser(UserCreateDto userDto);

    UserResponseDto getUser(Long userId);

    UserResponseDto updateUser(Long userId, UserUpdateDto userDto);

    void deleteUser(Long userId);
}
