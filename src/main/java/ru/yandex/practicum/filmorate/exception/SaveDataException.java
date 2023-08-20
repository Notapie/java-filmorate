package ru.yandex.practicum.filmorate.exception;

public class SaveDataException extends RuntimeException {
    public SaveDataException(String message) {
        super(message);
    }

    public SaveDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
