package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class FilmService {
    private final Map<Integer, Film> idToFilm = new TreeMap<>();
    int idGenerator = 1;

    public Collection<Film> getAll() {
        return idToFilm.values();
    }

    public Film create(final Film film) {
        validate(film);
        final Film newFilm = film.toBuilder().id(idGenerator++).build();

        idToFilm.put(newFilm.getId(), newFilm);
        log.debug("Added new film: " + newFilm);

        return newFilm;
    }

    public Film update(final Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Film id must be not null.");
        }
        if (!idToFilm.containsKey(film.getId())) {
            throw new FilmNotFoundException("Film with id " + film.getId() + " is not found.");
        }
        validate(film);

        final Film prevFilm = idToFilm.put(film.getId(), film);
        log.debug("Updated film: " + prevFilm + " -> " + film);

        return film;
    }

    private void validate(final Film film) {
        if (!StringUtils.hasText(film.getName())) {
            throw new ValidationException("film name must be not null or blank.");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("film desc must be less than 200.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Invalid film release date.");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Invalid film duration. Duration must be positive.");
        }
    }
}
