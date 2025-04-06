package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserCreateMapper;
import ru.practicum.shareit.user.mapper.UserResponseMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

@Slf4j
@Service("userService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        final Optional<User> userWithSameEmail = userRepository.findByEmail(userCreateDto.getEmail());
        if (userWithSameEmail.isPresent()) {
            final String errorMessage = String.format("The user with email=%s already exists in the database.",
                    userCreateDto.getEmail());
            log.warn(errorMessage);
            throw new DataAlreadyExistException(errorMessage);
        }
        User newUser = userRepository.save(userCreateMapper.toUser(userCreateDto));
        return userResponseMapper.toUserResponseDto(newUser);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        final Optional<User> currentUser = userRepository.findById(userId);
        if (currentUser.isEmpty()) {
            final String errorMessage = String.format("The user with id=%d not fount in the database.", userId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return userResponseMapper.toUserResponseDto(currentUser.get());
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        final UserResponseDto currentUser = getUser(userId);
        final String incomingUserName = userUpdateDto.getName();
        final String incomingUserEmail = userUpdateDto.getEmail();
        final Optional<User> userWithSameEmail = userRepository.findByEmail(incomingUserEmail);
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().equals(userResponseMapper.toUser(currentUser))) {
            final String errorMessage = String.format("The user with email=%s already exists in the database.",
                    incomingUserEmail);
            log.warn(errorMessage);
            throw new DataAlreadyExistException(errorMessage);
        }

        final String currentUserName = currentUser.getName();
        final String currentUserEmail = currentUser.getEmail();
        User newUser = User.builder()
                .id(userId)
                .name(incomingUserName == null || incomingUserName.isEmpty() ? currentUserName : incomingUserName)
                .email(incomingUserEmail == null ? currentUserEmail : incomingUserEmail)
                .build();
        userRepository.save(newUser);
        return userResponseMapper.toUserResponseDto(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
