package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserCreateMapperTest {

    @Autowired
    private UserCreateMapper userCreateMapper;

    @Test
    void toUser_whenInvokedWithUserCreateDto_thanReturnedUser() {
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .name("Антон Стародуб")
                .email("astarodub@ya.ru")
                .build();

        User mappedUser = userCreateMapper.toUser(userCreateDto);

        assertEquals(User.class, mappedUser.getClass());
        assertEquals(userCreateDto.getName(), mappedUser.getName());
        assertEquals(userCreateDto.getEmail(), mappedUser.getEmail());
    }

    @Test
    void toUserCreateDto_whenInvokedWithUser_thanReturnedUserCreateDto() {
        User user = User.builder()
                .id(100L)
                .name("Антон Стародуб")
                .email("astarodub@ya.ru")
                .build();

        UserCreateDto mappedDto = userCreateMapper.toUserCreateDto(user);

        assertEquals(UserCreateDto.class, mappedDto.getClass());
        assertEquals(user.getName(), mappedDto.getName());
        assertEquals(user.getEmail(), mappedDto.getEmail());
    }
}