package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ShareItServerTest {

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        ShareItServer.main(new String[] {});
    }

}