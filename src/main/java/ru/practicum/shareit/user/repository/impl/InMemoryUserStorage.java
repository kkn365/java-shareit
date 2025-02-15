package ru.practicum.shareit.user.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository("userRepository")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new LinkedHashMap<>();
    private long generatorId = 0;

    @Override
    public Optional<User> addNewUser(User user) {
        final Long userId = ++generatorId;
        final User newUser = User.builder()
                .id(userId)
                .name(user.getName())
                .email(user.getEmail())
                .build();
        users.put(userId, newUser);
        log.info("A new user has been added: {}.", newUser);
        return getUserById(userId);
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
    public Optional<User> getUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return users.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        log.info("User data has been updated: {}.", user);
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
        log.info("User with id={} deleted.", userId);
    }
}
