package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> createUser(UserDto userDto);

    Optional<UserDto> getUser(Long userId);

    Optional<UserDto> updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}
