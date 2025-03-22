package ru.practicum.shareit.booking.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemResponseMapper;
import ru.practicum.shareit.user.mapper.UserResponseMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingResponseMapper {

    public static BookingResponseDto toBookingResponseDto(Booking book) {
        return BookingResponseDto.builder()
                .id(book.getId())
                .start(book.getStart())
                .end(book.getEnd())
                .item(ItemResponseMapper.toItemResponseDto(book.getItem()))
                .booker(UserResponseMapper.toUserResponseDto(book.getBooker()))
                .status(book.getStatus())
                .build();
    }
}
