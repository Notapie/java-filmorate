package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAll() {
        return null;
    }

    public User create(final User user) {
        validate(user);

        final User.UserBuilder builder = user.toBuilder();
        if (user.getName() == null || user.getName().isBlank()) {
            builder.name(user.getLogin());
        }

        final User newUser = userStorage.createUser(builder.build());
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

        final User newUser = userStorage.updateUser(builder.build());
        log.debug("Updated user: " + newUser);

        return newUser;
    }

    private void validate(final User user) {
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
