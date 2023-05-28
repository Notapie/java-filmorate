package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilm(final int id) {
        final Film result = filmStorage.getFilmById(id);
        if (result == null) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
        return result;
    }

    public Collection<Film> getPopular(final int count) {
        final Collection<Film> sortedFilms = filmStorage.getFilmsSortedByLikes();

        final Collection<Film> result = new ArrayList<>();
        Iterator<Film> it = sortedFilms.iterator();
        for(int i = 0; i < count && it.hasNext(); i++) {
            result.add(it.next());
        }

        return result;
    }

    public void addLike(final int userId, final int filmId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(final int userId, final int filmId) {
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        filmStorage.removeLike(userId, filmId);
    }

    public Film create(final Film film) {
        validate(film);

        final Film newFilm = filmStorage.createFilm(film);
        log.debug("Added new film: " + newFilm);

        return newFilm;
    }

    public Film update(final Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Film id must be not null");
        }
        validate(film);

        final Film newFilm = filmStorage.updateFilm(film);
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
