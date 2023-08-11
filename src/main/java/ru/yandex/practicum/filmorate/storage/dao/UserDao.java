package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    // TODO: add exceptions and handlers for them
    @Override
    public User update(final User newObject) {
        final String sql = "UPDATE \"user\" " +
                "SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

        boolean isUpdated = jdbcTemplate.update(sql,
                newObject.getEmail(), newObject.getLogin(),
                newObject.getName(), newObject.getBirthday(),
                newObject.getId()) > 0;

        if (!isUpdated) {
            throw new NotFoundException("Film with id " + newObject.getId() + " is not found");
        }

        return getById(newObject.getId());
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
        final User userToRemove = getById(id);

        final String sql = "DELETE FROM \"user\" WHERE id = ?";
        jdbcTemplate.update(sql, id);

        return userToRemove;
    }

    @Override
    public Collection<User> getAll() {
        final String sql = "SELECT * FROM \"user\"";
        return jdbcTemplate.query(sql, (rs, rn) -> makeUser(rs));
    }

    @Override
    public User getById(final int id) {
        final String sql = "SELECT * FROM \"user\" WHERE id = ?";
        List<User> result = jdbcTemplate.query(sql, (rs, rn) -> makeUser(rs), id);

        if (result.isEmpty()) {
            throw new NotFoundException("User with id " + id + " is not found");
        }

        return result.get(0);
    }

    @Override
    public User getUserByEmail(final String email) {
        final String sql = "SELECT * FROM \"user\" WHERE email = ?";
        List<User> result = jdbcTemplate.query(sql, (rs, rn) -> makeUser(rs), email);

        if (result.isEmpty()) {
            throw new NotFoundException("User with email " + email + " is not found");
        }

        return result.get(0);
    }

    @Override
    public void linkAsFriends(final int firstUserId, final int secondUserId) {
        final String sql = "INSERT INTO \"user_friend\" " +
                "(user_id, friend_id, is_accepted) VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sql, firstUserId, secondUserId, false);
        } catch (DataAccessException e) {
            throw new NotFoundException("invalid friend id or user id");
        }
    }

    @Override
    public void unlinkFriends(final int firstUserId, final int secondUserId) {

    }

    @Override
    public Collection<User> getUserFriends(final int userId) {
        final String sql = "SELECT u.* FROM \"user_friend\" AS uf " +
                "INNER JOIN \"user\" AS u ON uf.friend_id = u.id " +
                "WHERE uf.user_id = ?";

        return jdbcTemplate.query(sql, (rs, rn) -> makeUser(rs), userId);
    }

    @Override
    public Collection<User> getMutualFriends(final int firstUserId, final int secondUserId) {
        final String sql = "SELECT u.* FROM \"user_friend\" AS uf " +
                "INNER JOIN \"user\" AS u ON uf.friend_id = u.id " +
                "WHERE uf.user_id = ? " +
                "AND uf.friend_id IN (SELECT friend_id FROM \"user_friend\" WHERE user_id = ?)";

        return jdbcTemplate.query(sql, (rs, rn) -> makeUser(rs), firstUserId, secondUserId);
    }

    private User makeUser(final ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}