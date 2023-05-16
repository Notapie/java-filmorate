package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> idToFilm = new TreeMap<>();

    @GetMapping
    public Collection<Film> get() {
        return idToFilm.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        if (idToFilm.containsKey(film.getId())) {
            throw new FilmAlreadyExistsException("Film with id " + film.getId() + " already exists.");
        }
        idToFilm.put(film.getId(), film);
        log.debug("Added new film: " + film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        if (!idToFilm.containsKey(film.getId())) {
            throw new FilmNotFoundException("Film with id " + film.getId() + " is not found.");
        }
        final Film prevFilm = idToFilm.put(film.getId(), film);
        log.debug("Updated film: " + prevFilm + " -> " + film);
        return film;
    }
}
