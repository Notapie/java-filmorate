package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User removeUser(int id);

    User removeUser(String email);

    User getUserByEmail(String email);

    User getUserById(int id);

    Collection<User> getUserFriends(int userId);

    Collection<User> getMutualFriends(int firstUserId, int secondUserId);
}
