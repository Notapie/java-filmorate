package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(final User user);

    User updateUser(final User user);

    User removeUser(final int id);

    User removeUser(final String email);

    User getUserByEmail(final String email);

    User getUserById(final int id);

    Collection<User> getUserFriends(final int userId);

    Collection<User> getMutualFriends(final int firstUserId, final int secondUserId);

}
