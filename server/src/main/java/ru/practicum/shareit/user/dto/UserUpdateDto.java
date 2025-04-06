package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode
public class UserUpdateDto {
    private String name;
    private String email;
}
