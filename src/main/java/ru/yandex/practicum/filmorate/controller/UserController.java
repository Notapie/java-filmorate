package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> idToUser = new HashMap<>();
    int idGenerator = 1;

    @GetMapping
    public Collection<User> get() {
        return idToUser.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        validate(user);

        final User.UserBuilder builder = user.toBuilder().id(idGenerator++);
        if (user.getName() == null || user.getName().isBlank()) {
            builder.name(user.getLogin());
        }
        final User newUser = builder.build();

        idToUser.put(newUser.getId(), newUser);
        log.debug("Added new user: " + newUser);

        return newUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        if (user.getId() == null) {
            throw new ValidationException("User id must be not null.");
        }
        if (!idToUser.containsKey(user.getId())) {
            throw new FilmNotFoundException("User with id " + user.getId() + " is not found.");
        }
        validate(user);

        final User.UserBuilder builder = user.toBuilder();
        if (user.getName() == null || user.getName().isBlank()) {
            builder.name(user.getLogin());
        }
        final User newUser = builder.build();

        final User prevFilm = idToUser.put(user.getId(), user);
        log.debug("Updated user: " + prevFilm + " -> " + user);

        return user;
    }

    private void validate(final User user) {
        if (!StringUtils.hasText(user.getLogin())) {
            throw new ValidationException("User login must be not null or blank.");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot contain spaces.");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("The date of birth cannot be in the future.");
        }
    }
}
