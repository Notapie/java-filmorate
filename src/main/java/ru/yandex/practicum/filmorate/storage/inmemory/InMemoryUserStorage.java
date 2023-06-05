package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> idToUser;
    private final Map<String, User> emailToUser;
    private final Map<String, User> loginToUser;
    private final Map<Integer, Set<Integer>> idToFriendsIds;
    int idGenerator;

    public InMemoryUserStorage() {
        this.idToUser = new HashMap<>();
        this.emailToUser = new HashMap<>();
        this.loginToUser = new HashMap<>();
        this.idToFriendsIds = new HashMap<>();
        this.idGenerator = 1;
    }

    @Override
    public User createUser(final User user) {
        if (emailToUser.containsKey(user.getEmail())) {
            throw new AlreadyExistsException(String.format(
                    "User with email %s already exists", user.getEmail()
            ));
        }

        if (loginToUser.containsKey(user.getLogin())) {
            throw new AlreadyExistsException(String.format(
                    "User with login %s already exists", user.getLogin()
            ));
        }

        final User newUser = user.toBuilder().id(idGenerator++).build();

        idToUser.put(newUser.getId(), newUser);
        emailToUser.put(newUser.getEmail(), newUser);
        loginToUser.put(newUser.getLogin(), newUser);
        idToFriendsIds.put(newUser.getId(), new HashSet<>());

        return newUser;
    }

    @Override
    public User updateUser(final User user) {
        final User oldRecord = idToUser.get(user.getId());

        if (oldRecord == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }

        if (!oldRecord.getEmail().equals(user.getEmail()) && emailToUser.containsKey(user.getEmail())) {
            throw new AlreadyExistsException(String.format(
                    "Email %s already exists", user.getEmail()
            ));
        }

        if (!oldRecord.getLogin().equals(user.getLogin()) && loginToUser.containsKey(user.getLogin())) {
            throw new AlreadyExistsException(String.format(
                    "Login %s already exists", user.getLogin()
            ));
        }

        idToUser.put(user.getId(), user);
        emailToUser.put(user.getEmail(), user);
        loginToUser.put(user.getLogin(), user);

        if (!oldRecord.getEmail().equals(user.getEmail())) {
            emailToUser.remove(oldRecord.getEmail());
        }
        if (!oldRecord.getLogin().equals(user.getLogin())) {
            loginToUser.remove(oldRecord.getLogin());
        }

        return user;
    }

    @Override
    public boolean linkAsFriends(final int firstUserId, final int secondUserId) {
        // check if users exists
        final Set<Integer> firstUserFriends = idToFriendsIds.get(firstUserId);
        final Set<Integer> secondUserFriends = idToFriendsIds.get(secondUserId);

        if (firstUserFriends == null) {
            throw new NotFoundException("User with id " + firstUserId + " not found");
        }
        if (secondUserFriends == null) {
            throw new NotFoundException("User with id " + secondUserId + " not found");
        }

        // saving
        secondUserFriends.add(firstUserId);
        firstUserFriends.add(secondUserId);
        return true;
    }

    @Override
    public boolean unlinkFriends(final int firstUserId, final int secondUserId) {
        final Set<Integer> firstUserFriends = idToFriendsIds.get(firstUserId);
        final Set<Integer> secondUserFriends = idToFriendsIds.get(secondUserId);

        if (firstUserFriends != null) {
            firstUserFriends.remove(secondUserId);
        }
        if (secondUserFriends != null) {
            secondUserFriends.remove(firstUserId);
        }

        return true;
    }

    @Override
    public User removeUser(final int id) {
        final User user = idToUser.remove(id);

        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }

        emailToUser.remove(user.getEmail());
        loginToUser.remove(user.getLogin());
        idToFriendsIds.remove(id);

        return user;
    }

    @Override
    public User getUserByEmail(final String email) {
        return emailToUser.get(email);
    }

    @Override
    public User getUserById(final int id) {
        return idToUser.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return idToUser.values();
    }

    @Override
    public Collection<User> getUserFriends(final int userId) {
        final Collection<Integer> userFriendsIds = idToFriendsIds.get(userId);

        if (userFriendsIds == null) {
            return Collections.emptyList();
        }

        final List<User> result = new ArrayList<>(userFriendsIds.size());
        for (final int friendId : userFriendsIds) {
            result.add(idToUser.get(friendId));
        }

        return result;
    }

    @Override
    public Collection<User> getMutualFriends(final int firstUserId, final int secondUserId) {
        final Collection<Integer> firstFriendsIds = idToFriendsIds.get(firstUserId);
        final Collection<Integer> secondFriendsIds = idToFriendsIds.get(secondUserId);

        if (firstFriendsIds == null || secondFriendsIds == null) {
            return Collections.emptyList();
        }

        // getting ids
        final Set<Integer> resultIds = new HashSet<>(firstFriendsIds);
        resultIds.retainAll(secondFriendsIds);

        // getting users
        final List<User> result = new ArrayList<>(resultIds.size());
        for (final int userId : resultIds) {
            result.add(idToUser.get(userId));
        }

        return result;
    }
}
