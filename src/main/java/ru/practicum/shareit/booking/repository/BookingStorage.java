package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND (b.status = ?2 AND b.start <= ?3 AND b.end >= ?3 )")
    Collection<Booking> findCurrentByOwnerId(Long ownerId, BookingStatus status, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND (b.status = ?2 AND b.end < ?3 )")
    Collection<Booking> findPastByOwnerId(Long ownerId, BookingStatus status, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND (b.status = ?2 AND b.start > ?3 )")
    Collection<Booking> findFutureByOwnerId(Long ownerId, BookingStatus status, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = ?2 ")
    Collection<Booking> findByOwnerIdAndStatus(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 ")
    Collection<Booking> findByOwnerId(Long ownerId);


    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND (b.status = ?2 AND b.start <= ?3 AND b.end >= ?3 )")
    Collection<Booking> findCurrentByBookerId(Long bookerId, BookingStatus status, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND (b.status = ?2 AND b.end < ?3 )")
    Collection<Booking> findPastByBookerId(Long bookerId, BookingStatus status, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND (b.status = ?2 AND b.start > ?3 )")
    Collection<Booking> findFutureByBookerId(Long bookerId, BookingStatus status, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = ?2 ")
    Collection<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 ")
    Collection<Booking> findByBookerId(Long bookerId);

    Collection<Booking> findAllByItemIdAndBookerId(Long itemId, Long userId);
}
