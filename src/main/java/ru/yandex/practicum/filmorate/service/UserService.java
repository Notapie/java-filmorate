package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        log.debug("Getting all users");
        return userStorage.getAll();
    }

    public User getUser(final int id) {
        log.debug("Getting user by id " + id);
        final User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " is not found");
        }
        return user;
    }

    public Collection<User> getUserFriends(final int userId) {
        log.debug("Getting user " + userId + " friends");
        return userStorage.getUserFriends(userId);
    }

    public Collection<User> getUsersCommonFriends(final int userId, final int otherId) {
        log.debug("Getting users " + userId + " and " + otherId + " common friends");
        return userStorage.getMutualFriends(userId, otherId);
    }

    public User create(final User user) {
        validate(user);

        final User.UserBuilder builder = user.toBuilder();
        if (user.getName() == null || user.getName().isBlank()) {
            builder.name(user.getLogin());
        }

        final User newUser = userStorage.create(builder.build());
        log.debug("Added new user: " + newUser);

        return newUser;
    }


    public User update(final User user) {
        if (user.getId() == null) {
            throw new ValidationException("User id must be not null");
        }
        validate(user);

        final User.UserBuilder builder = user.toBuilder();
        if (user.getName() == null || user.getName().isBlank()) {
            builder.name(user.getLogin());
        }

        final User newUser = userStorage.update(builder.build());
        log.debug("Updated user: " + newUser);

        return newUser;
    }

    public void linkFriends(final int userId, final int otherId) {
        log.debug("Sending the friendship request from " + userId + " to " + otherId);
        userStorage.linkAsFriends(userId, otherId);
    }

    public void unlinkFriends(final int userId, final int otherId) {
        log.debug("Canceling the friendship request from " + userId + " to " + otherId);
        userStorage.unlinkFriends(userId, otherId);
    }

    private void validate(final User user) {
        if (user.getEmail() == null || !EmailValidator.validate(user.getEmail())) {
            throw new ValidationException("Invalid email address");
        }

        if (!StringUtils.hasText(user.getLogin())) {
            throw new ValidationException("User login must be not null or blank");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot contain spaces");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("The date of birth cannot be in the future");
        }
    }
}
