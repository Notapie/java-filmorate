package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @PostMapping
    public Mpa create(@RequestBody final Mpa newMpa) {
        return service.createNewMpa(newMpa);
    }

    @PutMapping
    public Mpa update(@RequestBody final Mpa newMpa) {
        return service.updateMpa(newMpa);
    }

    @GetMapping
    public Collection<Mpa> getAll() {
        return service.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable final int id) {
        return service.getMpaById(id);
    }
}
