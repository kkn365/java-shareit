package ru.practicum.shareit.user.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository("userRepository")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new LinkedHashMap<>();
    final Set<String> emailUniqueSet = new HashSet<>();
    private long generatorId = 0;

    @Override
    public User addNewUser(UserCreateDto user) {
        final String email = user.getEmail();
        if (emailUniqueSet.contains(email)) {
            final String message = String.format("Email: %s already exists.", email);
            log.warn(message);
            throw new DataAlreadyExistException(message);
        }
        final Long userId = ++generatorId;
        final User newUser = User.builder()
                .id(userId)
                .name(user.getName())
                .email(user.getEmail())
                .build();
        users.put(userId, newUser);
        emailUniqueSet.add(email);
        log.info("A new user has been added: {}.", newUser);
        return newUser;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        final User user = users.get(userId);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public void updateUser(User user) {
        final String email = user.getEmail();
        users.computeIfPresent(user.getId(), (id, u) -> {
                    if (!email.equals(u.getEmail())) {
                        if (emailUniqueSet.contains(email)) {
                            final String message = String.format("Email: %s already exists.", email);
                            log.warn(message);
                            throw new DataAlreadyExistException(message);
                        }
                        emailUniqueSet.remove(u.getEmail());
                        emailUniqueSet.add(email);
                    }
                    log.info("User data has been updated: {}.", user);
                    return user;
                }
        );
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
        log.info("User with id={} deleted.", userId);
    }
}
