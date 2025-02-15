package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage {
    Optional<User> addNewUser(User user);

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String userEmail);

    Optional<User> getUserByName(String userName);

    void updateUser(User user);

    void deleteUser(Long userId);
}
