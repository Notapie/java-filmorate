package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Component
@Primary
@RequiredArgsConstructor
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public User update(final User newObject) {
        final String sql = "UPDATE \"user\" " +
                "SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                newObject.getName(), newObject.getLogin(),
                newObject.getName(), newObject.getBirthday(),
                newObject.getId());

        return newObject;
    }

    @Override
    public User create(final User newObject) {
        final String sql = "INSERT INTO \"user\" (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newObject.getEmail());
            ps.setString(2, newObject.getLogin());
            ps.setString(3, newObject.getName());
            ps.setString(4, newObject.getBirthday().toString());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Error getting the new user id");
        }
        return newObject.toBuilder().id(keyHolder.getKey().intValue()).build();
    }

    @Override
    public User delete(final int id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public User getById(final int id) {
        return null;
    }

    @Override
    public void linkAsFriends(final int firstUserId, final int secondUserId) {

    }

    @Override
    public void unlinkFriends(final int firstUserId, final int secondUserId) {

    }

    @Override
    public User getUserByEmail(final String email) {
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
