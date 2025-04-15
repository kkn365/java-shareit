package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantsTest {

    @Test
    void testConstantsValues() {
        assertEquals("X-Sharer-User-Id", Constants.USER_ID_HEADER);
        assertEquals("ALL", Constants.DEFAULT_SEARCH_VALUE);
    }
}