package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage {
    User addNewUser(User user);

    Optional<User> getUserById(Long userId);

    void updateUser(User user);

    void deleteUser(Long userId);
}
