package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataDuplicaionException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
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
    public Optional<UserDto> createUser(UserDto userDto) {
        final String name = userDto.getName();
        final String email = userDto.getEmail();

        if (name == null) {
            final String errorMessage = "The user name must be set.";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (userStorage.getUserByName(name).isPresent()) {
            final String errorMessage = String.format("The user with name=%s is already in the database.", email);
            log.warn(errorMessage);
            throw new DataDuplicaionException(errorMessage);
        }

        if (email == null) {
            final String errorMessage = "The user email must be set.";
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (userStorage.getUserByEmail(email).isPresent()) {
            final String errorMessage = String.format("The user with email=%s is already in the database.", email);
            log.warn(errorMessage);
            throw new DataDuplicaionException(errorMessage);
        }

        final User user = UserMapper.toUser(userDto);
        final User newUser = userStorage.addNewUser(user).orElseThrow(InternalServerException::new);
        return Optional.of(UserMapper.toUserDto(newUser));
    }

    @Override
    public Optional<UserDto> getUser(Long userId) {
        final Optional<User> currentUser = userStorage.getUserById(userId);
        if (currentUser.isEmpty()) {
            final String errorMessage = String.format("The user with id=%d not fount in the database.", userId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return Optional.of(UserMapper.toUserDto(currentUser.get()));
    }

    @Override
    public Optional<UserDto> updateUser(Long userId, UserDto userDto) {
        final Optional<UserDto> currentUser = getUser(userId);
        final String currentUserName = currentUser.get().getName();
        final String currentUserMail = currentUser.get().getEmail();

        final String incomingUserName = userDto.getName();
        final String incomingUserMail = userDto.getEmail();

        if (!currentUserName.equals(incomingUserName)) {
            final Optional<User> userWithSameName = userStorage.getUserByName(incomingUserName);
            if (userWithSameName.isPresent() && !userId.equals(userWithSameName.get().getId())) {
                final String errorMessage = String.format("The user with same name=%s is already in the database.",
                        incomingUserName);
                log.warn(errorMessage);
                throw new DataDuplicaionException(errorMessage);
            }
        }

        if (!currentUserMail.equals(incomingUserMail)) {
            final Optional<User> userWithSameEmail = userStorage.getUserByEmail(incomingUserMail);
            if (userWithSameEmail.isPresent() && !userId.equals(userWithSameEmail.get().getId())) {
                final String errorMessage = String.format("The user with same email=%s is already in the database.",
                        incomingUserMail);
                log.warn(errorMessage);
                throw new DataDuplicaionException(errorMessage);
            }
        }

        final User updatedUser = User.builder()
                .id(userId)
                .name(incomingUserName == null ? currentUserName : incomingUserName)
                .email(incomingUserMail == null ? currentUserMail : incomingUserMail)
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
