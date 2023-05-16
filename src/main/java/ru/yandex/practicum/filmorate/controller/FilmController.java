package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> idToFilm = new TreeMap<>();

    @GetMapping
    public Collection<Film> GetFilms() {
        return idToFilm.values();
    }

    @PostMapping
    public Film AddFilm(@RequestBody Film film) {
        if (idToFilm.containsKey(film.getId())) {
            throw new FilmAlreadyExistsException(film + " already exists.");
        }
        idToFilm.put(film.getId(), film);
        log.debug("Added new film " + film);
        return film;
    }

    @PutMapping
    public Film UpdateFilm(@RequestBody Film film) {
        if (!idToFilm.containsKey(film.getId())) {
            throw new FilmNotFoundException(film + " is not found.");
        }
        idToFilm.put(film.getId(), film);
        log.debug("Updated film " + film);
        return film;
    }
}
