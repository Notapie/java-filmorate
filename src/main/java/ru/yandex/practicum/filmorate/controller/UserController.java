package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> get() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable final int userId) {
        return service.getUser(userId);
    }

    @PostMapping
    public User create(@RequestBody final User user) {
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody final User user) {
        return service.update(user);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable final int userId) {
        return service.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getUsersCommonFriends(@PathVariable final int userId, @PathVariable final int otherId) {
        return service.getUsersCommonFriends(userId, otherId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable final int userId, @PathVariable final int friendId) {
        service.linkFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable final int userId, @PathVariable final int friendId) {
        service.unlinkFriends(userId, friendId);
    }
}
