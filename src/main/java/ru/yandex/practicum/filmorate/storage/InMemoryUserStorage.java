package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> idToUser;
    int idGenerator;

    public InMemoryUserStorage() {
        this.idToUser = new HashMap<>();
        this.idGenerator = 1;
    }

    @Override
    public User createUser(final User user) {
        if (idToUser.containsKey(user.getId())) {
            //TODO: Переделать на кастомное исключение
            throw new RuntimeException();
        }

        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(final User user) {
        return null;
    }

    @Override
    public User removeUser(final int id) {
        return null;
    }

    @Override
    public User removeUser(final String email) {
        return null;
    }

    @Override
    public User getUserByEmail(final String email) {
        return null;
    }

    @Override
    public User getUserById(final int id) {
        return null;
    }

    @Override
    public Collection<User> getUserFriends(final int userId) {
        return null;
    }

    @Override
    public Collection<User> getMutualFriends(final int firstUserId, final int secondUserId) {
        return null;
    }
}
