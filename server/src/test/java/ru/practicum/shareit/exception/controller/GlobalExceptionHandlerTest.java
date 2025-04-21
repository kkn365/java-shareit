package ru.practicum.shareit.exception.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.model.ErrorResponse;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleValidation() {
        ValidationException exception = new ValidationException("Parameter not valid");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(exception.getMessage(), Objects.requireNonNull(response.getBody()).description());
    }

    @Test
    void handleNotFound() {
        NotFoundException exception = new NotFoundException("Parameter not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(exception.getMessage(), Objects.requireNonNull(response.getBody()).description());
    }

    @Test
    void handleInternalServerException() {
        InternalServerException exception = new InternalServerException("Internal error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInternalServerException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(exception.getMessage(), Objects.requireNonNull(response.getBody()).description());
    }

    @Test
    void handleDataAlreadyExistException() {
        DataAlreadyExistException exception = new DataAlreadyExistException("Data already exists");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataAlreadyExistException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(exception.getMessage(), Objects.requireNonNull(response.getBody()).description());
    }

    @Test
    void handleForbidden() {
        ForbiddenException exception = new ForbiddenException("Access forbidden.");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleForbidden(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(exception.getMessage(), Objects.requireNonNull(response.getBody()).description());
    }
}