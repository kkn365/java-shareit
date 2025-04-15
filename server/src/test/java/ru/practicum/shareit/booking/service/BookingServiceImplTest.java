package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BookingServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void init() {
        owner = userRepository.save(User.builder()
                .name("Test owner")
                .email("owner@mail.ru")
                .build());
        booker = userRepository.save(User.builder()
                .name("Test booker")
                .email("booker@mail.ru")
                .build());
        item = itemRepository.save(Item.builder()
                .name("Test item")
                .description("Test item description")
                .available(true)
                .owner(owner)
                .build());
    }

    @Test
    void createBook_whenInvoked_thenReturnedBookingResponseDtoWithWaitingStatus() {
        final LocalDateTime start = LocalDateTime.now().plusDays(1);
        final LocalDateTime end = LocalDateTime.now().plusDays(2);
        final BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .build();

        BookingResponseDto result = bookingService.createBook(booker.getId(), bookingCreateDto);

        assertEquals(bookingCreateDto.getItemId(), result.getItem().getId());
        assertEquals(bookingCreateDto.getStart(), start);
        assertEquals(bookingCreateDto.getEnd(), end);
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void createBook_whenInvokedWithNotAvailableItem_thenInternalServerExceptionThrows() {
        final LocalDateTime start = LocalDateTime.now().plusDays(1);
        final LocalDateTime end = LocalDateTime.now().plusDays(2);
        final BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .build();
        item.setAvailable(false);
        itemRepository.save(item);

        assertThrows(InternalServerException.class, () ->
                bookingService.createBook(booker.getId(), bookingCreateDto));
    }

    @Test
    void updateBookingStatus_whenInvokedWithIncorrectUser_thenValidationExceptionThrows() {
        final long increment = 1000L;
        assertThrows(ValidationException.class, () ->
                bookingService.updateBookingStatus(owner.getId() + increment, increment, true));
    }

    @Test
    void updateBookingStatus_whenInvokedWithIncorrectBooking_thenNotFoundExceptionThrows() {
        final long increment = 1000L;
        assertThrows(NotFoundException.class, () ->
                bookingService.updateBookingStatus(owner.getId(), increment, true));
    }

    @Test
    void updateBookingStatus_whenInvokedWithNotOwner_thenForbiddenExceptionThrows() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        assertThrows(ForbiddenException.class, () ->
                bookingService.updateBookingStatus(booker.getId(), booking.getId(), true));
    }

    @Test
    void updateBookingStatus_whenInvokedByOwnerWithApprovedStatus_thenReturnedApprovedBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        BookingResponseDto result = bookingService.updateBookingStatus(owner.getId(), booking.getId(), true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void updateBookingStatus_whenInvokedByOwnerWithRejectedStatus_thenReturnedRejectedBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        BookingResponseDto result = bookingService.updateBookingStatus(owner.getId(), booking.getId(), false);

        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void getBookingById_whenInvokedWithIncorrectUser_thenValidationExceptionThrows() {
        final long increment = 1000L;
        assertThrows(ValidationException.class, () ->
                bookingService.getBookingById(owner.getId() + increment, increment));
    }

    @Test
    void getBookingById_whenInvokedWithIncorrectBooking_thenNotFoundExceptionThrows() {
        final long increment = 1000L;
        assertThrows(NotFoundException.class, () ->
                bookingService.getBookingById(owner.getId(), increment));
    }

    @Test
    void getBookingById_whenInvokedWithNotOwner_thenForbiddenExceptionThrows() {
        final User user = userRepository.save(User.builder()
                .name("Test user 3")
                .email("emailTestUser3@email.com")
                .build());
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        assertThrows(ForbiddenException.class, () ->
                bookingService.getBookingById(user.getId(), booking.getId()));
    }

    @Test
    void getBookingById_whenInvokedWithBooker_thenReturnedBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        BookingResponseDto result = bookingService.getBookingById(booker.getId(), booking.getId());

        assertEquals(booking.getBooker().getName(), result.getBooker().getName());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenReturnedCurrentBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getItemsBookingsListForCurrentUser(owner.getId(),
                BookingState.CURRENT);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenReturnedPastBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusDays(2))
                .status(BookingStatus.APPROVED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getItemsBookingsListForCurrentUser(owner.getId(),
                BookingState.PAST);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenReturnedFutureBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getItemsBookingsListForCurrentUser(owner.getId(),
                BookingState.FUTURE);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenReturnedWaitingBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        Collection<BookingResponseDto> result = bookingService.getItemsBookingsListForCurrentUser(owner.getId(),
                BookingState.WAITING);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenReturnedRejectedBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.REJECTED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getItemsBookingsListForCurrentUser(owner.getId(),
                BookingState.REJECTED);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getItemsBookingsListForCurrentUser_whenInvoked_thenReturnedAllBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.REJECTED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getItemsBookingsListForCurrentUser(owner.getId(),
                BookingState.ALL);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvokedByBooker_thenReturnedCurrentBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getBookingsListForCurrentUser(booker.getId(),
                BookingState.CURRENT);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvokedByBooker_thenReturnedPastBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .status(BookingStatus.APPROVED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getBookingsListForCurrentUser(booker.getId(),
                BookingState.PAST);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvokedByBooker_thenReturnedFutureBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getBookingsListForCurrentUser(booker.getId(),
                BookingState.FUTURE);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvokedByBooker_thenReturnedWaitingBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build());

        Collection<BookingResponseDto> result = bookingService.getBookingsListForCurrentUser(booker.getId(),
                BookingState.WAITING);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvokedByBooker_thenReturnedRejectedBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.REJECTED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getBookingsListForCurrentUser(booker.getId(),
                BookingState.REJECTED);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void getBookingsListForCurrentUser_whenInvokedByBooker_thenReturnedALLBookingResponseDto() {
        final Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.REJECTED)
                .build());

        Collection<BookingResponseDto> result = bookingService.getBookingsListForCurrentUser(booker.getId(),
                BookingState.ALL);

        assertEquals(booking.getId(), result.stream().findFirst().get().getId());
    }

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }
}