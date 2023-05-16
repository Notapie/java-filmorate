package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> idToUser = new HashMap<>();

    @GetMapping
    public Collection<User> get() {
        return idToUser.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        if (idToUser.containsKey(user.getId())) {
            throw new FilmAlreadyExistsException("User with id " + user.getId() + " already exists.");
        }
        idToUser.put(user.getId(), user);
        log.debug("Added new user: " + user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        if (!idToUser.containsKey(user.getId())) {
            throw new FilmNotFoundException("User with id " + user.getId() + " is not found.");
        }
        final User prevFilm = idToUser.put(user.getId(), user);
        log.debug("Updated user: " + prevFilm + " -> " + user);
        return user;
    }
}
