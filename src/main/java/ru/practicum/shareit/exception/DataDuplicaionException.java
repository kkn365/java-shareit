package ru.practicum.shareit.exception;

public class DataDuplicaionException extends RuntimeException {
    public DataDuplicaionException() {
    }

    public DataDuplicaionException(String message) {
        super(message);
    }

    public DataDuplicaionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDuplicaionException(Throwable cause) {
        super(cause);
    }
}
