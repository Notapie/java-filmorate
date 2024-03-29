package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage extends StorageBase<User> {

    void linkAsFriends(int firstUserId, int secondUserId);

    void unlinkFriends(int firstUserId, int secondUserId);

    User getUserByEmail(String email);

    Collection<User> getUserFriends(int userId);

    Collection<User> getMutualFriends(int firstUserId, int secondUserId);
}
