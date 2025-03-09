package ru.practicum.shareit.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class UserUpdateDto {
    @Nullable
    private String name;
    @Email
    private String email;
}
