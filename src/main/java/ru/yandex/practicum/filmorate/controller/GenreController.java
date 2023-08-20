package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @PostMapping
    public Genre create(@RequestBody final Genre newGenre) {
        return service.createNewGenre(newGenre);
    }

    @PutMapping
    public Genre update(@RequestBody final Genre newGenre) {
        return service.updateGenre(newGenre);
    }

    @GetMapping
    public Collection<Genre> getAll() {
        return service.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable final int id) {
        return service.getGenreById(id);
    }
}
