package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUser(userId));
    }

    @PostMapping
    ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }

    @PatchMapping("/{userId}")
    ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUser(userId, user));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
