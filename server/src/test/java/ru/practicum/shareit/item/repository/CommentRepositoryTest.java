package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAllByItemId() {
        User owner = userRepository.save(User.builder()
                .name("Иван Иванов")
                .email("ivan@ai.com")
                .build()
        );
        User author = userRepository.save(User.builder()
                .name("Лилия Смирнова")
                .email("lilysmir@mail.ru")
                .build()
        );
        Item item = itemRepository.save(Item.builder()
                .name("Рожковый ключ на 72")
                .description("Бесполезная вещь")
                .available(true)
                .owner(owner)
                .build()
        );
        Comment comment = commentRepository.save(Comment.builder()
                .text("Действительно абсолютно бесполезная вещь.")
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build()
        );

        Collection<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertEquals(1, comments.size());
        assertTrue(comments.contains(comment));
    }

    @AfterEach
    public void deleteData() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}