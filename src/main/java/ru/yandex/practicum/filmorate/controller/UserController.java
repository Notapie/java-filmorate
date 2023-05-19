package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> get() {
        return service.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        return service.update(user);
    }
}
