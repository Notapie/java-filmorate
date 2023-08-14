package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final int MAX_TITLE_LENGTH;
    private final int MAX_DESC_LENGTH;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Value("${max.film.title.length}") int maxTitleLen,
                       @Value("${max.film.desc.length}") int maxDescLen,
                       FilmStorage filmStorage, UserStorage userStorage) {
        this.MAX_TITLE_LENGTH = maxTitleLen;
        this.MAX_DESC_LENGTH = maxDescLen;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilm(final int id) {
        final Film result = filmStorage.getById(id);
        if (result == null) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
        return result;
    }

    public Collection<Film> getPopular(final int limit) {
        return filmStorage.getFilmsSortedByLikes(limit);
    }

    public void addLike(final int userId, final int filmId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(final int userId, final int filmId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        filmStorage.removeLike(userId, filmId);
    }

    public Film create(final Film film) {
        validate(film);

        final Film newFilm = filmStorage.create(film);
        log.debug("Added new film: " + newFilm);

        return newFilm;
    }

    public Film update(final Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Film id must be not null");
        }
        validate(film);

        final Film newFilm = filmStorage.update(film);
        log.debug("Updated film: " + newFilm);

        return newFilm;
    }

    private void validate(final Film film) {
        if (!StringUtils.hasText(film.getName())) {
            throw new ValidationException("Film title must be not null or blank");
        }

        if (film.getName().length() > MAX_TITLE_LENGTH) {
            throw new ValidationException("Film title must be less than " + MAX_TITLE_LENGTH);
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESC_LENGTH) {
            throw new ValidationException("Film desc must be less than " + MAX_DESC_LENGTH);
        }

        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Invalid film release date");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Invalid film duration. Duration must be positive");
        }
    }
}
