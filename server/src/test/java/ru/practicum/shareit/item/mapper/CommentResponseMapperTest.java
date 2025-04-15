package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentResponseMapperTest {

    @Autowired
    private CommentResponseMapper commentResponseMapper;

    @Test
    void toCommentResponseDto() {
        final long itemId = 1L;
        final long userId = 1L;
        final long commentId = 1L;
        User owner = User.builder()
                .id(userId)
                .name("Name")
                .email("Name@email.com")
                .build();
        User user = User.builder()
                .id(userId + 1)
                .name("User")
                .email("user@email.com")
                .build();
        Item item = Item.builder()
                .id(itemId)
                .name("Test item")
                .description("Test description")
                .owner(owner)
                .available(true)
                .build();
        Comment comment = Comment.builder()
                .id(commentId)
                .text("Test comment")
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        CommentResponseDto result = commentResponseMapper.toCommentResponseDto(comment);

        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getItem().getId(), result.getItemId());
        assertEquals(comment.getAuthor().getName(), result.getAuthorName());
    }
}