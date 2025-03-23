package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    User toUser(UserResponseDto userResponseDto);

    UserResponseDto toUserResponseDto(User user);
}
