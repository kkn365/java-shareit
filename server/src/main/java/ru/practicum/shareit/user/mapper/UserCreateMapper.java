package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.model.User;

@Component
@Mapper(componentModel = "spring")
public interface UserCreateMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserCreateDto userCreateDto);

    UserCreateDto toUserCreateDto(User user);
}
