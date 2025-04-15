package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

@Component
@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    User toUser(UserResponseDto userResponseDto);

    UserResponseDto toUserResponseDto(User user);
}
