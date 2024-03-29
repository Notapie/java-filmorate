package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> get() {
        return service.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable final int filmId) {
        return service.getFilm(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return service.getPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody final Film film) {
        return service.create(film);
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        return service.update(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void like(@PathVariable final int filmId, @PathVariable final int userId) {
        service.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void unlike(@PathVariable final int filmId, @PathVariable final int userId) {
        service.removeLike(userId, filmId);
    }
}
