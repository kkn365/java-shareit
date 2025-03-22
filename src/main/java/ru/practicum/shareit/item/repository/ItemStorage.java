package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage extends JpaRepository<Item, Long> {

    @Query("SELECT it FROM Item it WHERE it.owner.id = ?1 ")
    Collection<Item> findByOwnerId(Long userId);

    @Query("SELECT it FROM Item it " +
           "WHERE it.available = true " +
           "AND (" +
           "UPPER(it.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
           "OR " +
           "UPPER(it.description) LIKE UPPER(CONCAT('%', ?1, '%'))" +
           ") ")
    Collection<Item> findAvailableWithSearchInFieldNameOrDescription(String text);
}
