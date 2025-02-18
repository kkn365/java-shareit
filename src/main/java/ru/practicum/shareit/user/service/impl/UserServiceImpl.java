package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

@Slf4j
@Service("userService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        final User newUser = userStorage.addNewUser(userCreateDto);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        final Optional<User> currentUser = userStorage.getUserById(userId);
        if (currentUser.isEmpty()) {
            final String errorMessage = String.format("The user with id=%d not fount in the database.", userId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return UserMapper.toUserDto(currentUser.get());
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        final UserResponseDto currentUser = getUser(userId);
        final User updatedUser = User.builder()
                .id(userId)
                .name(userUpdateDto.getName() == null || userUpdateDto.getName().isEmpty() ? currentUser.getName()
                        : userUpdateDto.getName())
                .email(userUpdateDto.getEmail() == null ? currentUser.getEmail() : userUpdateDto.getEmail())
                .build();
        userStorage.updateUser(updatedUser);
        return getUser(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        getUser(userId);
        userStorage.deleteUser(userId);
    }

}
