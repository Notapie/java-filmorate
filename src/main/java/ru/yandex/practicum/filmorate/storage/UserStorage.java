package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    boolean linkAsFriends(int firstUserId, int secondUserId);

    boolean unlinkFriends(int firstUserId, int secondUserId);

    User removeUser(int id);

    User getUserByEmail(String email);

    User getUserById(int id);

    Collection<User> getUserFriends(int userId);

    Collection<User> getMutualFriends(int firstUserId, int secondUserId);
}
