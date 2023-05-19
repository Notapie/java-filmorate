package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> get() {
        return service.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        return service.create(film);
    }

    @PutMapping
    public Film update(@RequestBody final Film film) {
        return service.update(film);
    }
}
