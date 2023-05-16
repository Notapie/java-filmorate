package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> idToFilm = new TreeMap<>();
    int idGenerator = 1;

    @GetMapping
    public Collection<Film> get() {
        return idToFilm.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        validate(film);
        final Film newFilm = film.toBuilder().id(idGenerator++).build();

        idToFilm.put(newFilm.getId(), newFilm);
        log.debug("Added new film: " + newFilm);

        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Invalid film release date.");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Invalid film duration. Duration must be positive.");
        }
    }
}
