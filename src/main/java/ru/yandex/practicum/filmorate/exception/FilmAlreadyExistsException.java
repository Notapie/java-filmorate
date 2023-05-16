package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String message) {
        super(message);
    }

    public FilmAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
