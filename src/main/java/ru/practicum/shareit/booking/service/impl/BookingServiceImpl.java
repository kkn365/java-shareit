package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingResponseMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemResponseMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserResponseMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service("bookingService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingResponseDto createBook(Long userId, BookingCreateDto book) {
        final UserResponseDto user = userService.getUser(userId);
        final ItemResponseDto item = itemService.getItem(userId, book.getItemId());
        if (!item.getAvailable()) {
            final String errorMessage = String.format("The item with id=%d not available.", item.getId());
            log.warn(errorMessage);
            throw new InternalServerException(errorMessage);
        }
        final Booking newBooking = Booking.builder()
                .start(book.getStart())
                .end(book.getEnd())
                .item(ItemResponseMapper.toItem(item))
                .booker(UserResponseMapper.toUser(user))
                .status(BookingStatus.WAITING)
                .build();
        final Booking addedBooking = bookingStorage.save(newBooking);
        return BookingResponseMapper.toBookingResponseDto(addedBooking);
    }

    @Transactional
    @Override
    public BookingResponseDto updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        final Optional<User> currentUser = userStorage.findById(userId);
        if (currentUser.isEmpty()) {
            final String errorMessage = String.format("Incorrect user id=%d value.", userId);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
        Optional<Booking> currentBooking = bookingStorage.findById(bookingId);
        if (currentBooking.isEmpty()) {
            final String errorMessage = String.format("The booking with id=%d not fount in the database.", bookingId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        if (!currentBooking.get().getItem().getOwner().getId().equals(userId)) {
            final String errorMessage = String.format("Access forbidden for user id=%d.", userId);
            log.warn(errorMessage);
            throw new ForbiddenException(errorMessage);
        }
        if (approved) {
            currentBooking.get().setStatus(BookingStatus.APPROVED);
        } else {
            currentBooking.get().setStatus(BookingStatus.REJECTED);
        }
        Booking savedBooking = bookingStorage.save(currentBooking.get());
        return BookingResponseMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    public BookingResponseDto getBookingById(Long userId, Long bookingId) {
        final Optional<User> currentUser = userStorage.findById(userId);
        if (currentUser.isEmpty()) {
            final String errorMessage = String.format("Incorrect user id=%d value.", userId);
            log.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
        Optional<Booking> currentBooking = bookingStorage.findById(bookingId);
        if (currentBooking.isEmpty()) {
            final String errorMessage = String.format("The booking with id=%d not fount in the database.", bookingId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        final Long bookerId = currentBooking.get().getBooker().getId();
        final Long ownerId = currentBooking.get().getItem().getOwner().getId();
        if ((bookerId.equals(userId)) || (ownerId.equals(userId))) {
            return BookingResponseMapper.toBookingResponseDto(currentBooking.get());
        }
        final String errorMessage = String.format("Access forbidden for user id=%d.", userId);
        log.warn(errorMessage);
        throw new ForbiddenException(errorMessage);
    }

    @Override
    public Collection<BookingResponseDto> getItemsBookingsListForCurrentUser(Long userId, BookingState state) {
        userService.getUser(userId);
        switch (state) {
            case CURRENT -> {
                return bookingStorage.findCurrentByOwnerId(userId, BookingStatus.APPROVED, LocalDateTime.now())
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case PAST -> {
                return bookingStorage.findPastByOwnerId(userId, BookingStatus.APPROVED, LocalDateTime.now())
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case FUTURE -> {
                return bookingStorage.findFutureByOwnerId(userId, BookingStatus.APPROVED, LocalDateTime.now())
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case WAITING -> {
                return bookingStorage.findByOwnerIdAndStatus(userId, BookingStatus.WAITING)
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case REJECTED -> {
                return bookingStorage.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED)
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            default -> {
                return bookingStorage.findByOwnerId(userId)
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
        }
    }

    @Override
    public Collection<BookingResponseDto> getBookingsListForCurrentUser(Long userId, BookingState state) {
        userService.getUser(userId);
        switch (state) {
            case CURRENT -> {
                return bookingStorage.findCurrentByBookerId(userId, BookingStatus.APPROVED, LocalDateTime.now())
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case PAST -> {
                return bookingStorage.findPastByBookerId(userId, BookingStatus.APPROVED, LocalDateTime.now())
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case FUTURE -> {
                return bookingStorage.findFutureByBookerId(userId, BookingStatus.APPROVED, LocalDateTime.now())
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case WAITING -> {
                return bookingStorage.findByBookerIdAndStatus(userId, BookingStatus.WAITING)
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            case REJECTED -> {
                return bookingStorage.findByBookerIdAndStatus(userId, BookingStatus.REJECTED)
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
            default -> {
                return bookingStorage.findByBookerId(userId)
                        .stream()
                        .map(BookingResponseMapper::toBookingResponseDto)
                        .toList();
            }
        }
    }

}
