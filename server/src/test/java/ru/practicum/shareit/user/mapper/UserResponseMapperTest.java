package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserResponseMapperTest {

    @Autowired
    private UserResponseMapper userResponseMapper;

    @Test
    void toUser_whenInvokedWithUserResponseDto_whenReturnedUser() {
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1000L)
                .name("Антонио Фагундес")
                .email("fagundesa@gmail.ru")
                .build();

        User mappedUser = userResponseMapper.toUser(userResponseDto);

        assertEquals(User.class, mappedUser.getClass());
        assertEquals(userResponseDto.getId(), mappedUser.getId());
        assertEquals(userResponseDto.getName(), mappedUser.getName());
        assertEquals(userResponseDto.getEmail(), mappedUser.getEmail());
    }

    @Test
    void toUserResponseDto_whenInvokedWithUser_whenReturnedUserResponseDto() {
        User user = User.builder()
                .id(1000L)
                .name("Антонио Фагундес")
                .email("fagundesa@gmail.ru")
                .build();

        UserResponseDto mappedDto = userResponseMapper.toUserResponseDto(user);

        assertEquals(UserResponseDto.class, mappedDto.getClass());
        assertEquals(user.getId(), mappedDto.getId());
        assertEquals(user.getName(), mappedDto.getName());
        assertEquals(user.getEmail(), mappedDto.getEmail());
    }
}