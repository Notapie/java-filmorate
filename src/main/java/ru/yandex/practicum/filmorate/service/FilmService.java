package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

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
        log.debug("Updated film: " + film);

        return newFilm;
    }

    private void validate(final Film film) {
        if (!StringUtils.hasText(film.getName())) {
            throw new ValidationException("film name must be not null or blank");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("film desc must be less than 200");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Invalid film release date");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Invalid film duration. Duration must be positive");
        }
    }
}
