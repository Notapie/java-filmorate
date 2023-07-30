package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    // TODO: add exceptions and handlers for them
    @Override
    public void addLike(int userId, int filmId) {
        final String sql = "INSERT INTO \"user_favorite\" (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void removeLike(int userId, int filmId) {
        final String sql = "DELETE FROM \"user_favorite\" WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public Film update(Film newObject) {
        final String sql = "UPDATE \"film\" " +
                "SET name = ?, description = ?, release_date = ?, duration = ? WHERE film_id = ?";

        jdbcTemplate.update(sql,
                newObject.getName(), newObject.getDescription(),
                newObject.getReleaseDate().toString(), newObject.getDuration(),
                newObject.getId());

        return newObject;
    }

    @Override
    public Film create(Film newObject) {
        final String sql = "INSERT INTO \"film\" (title, description, release_date, duration) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newObject.getName());
            ps.setString(2, newObject.getDescription());
            ps.setString(3, newObject.getReleaseDate().toString());
            ps.setInt(4, newObject.getDuration());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Error getting the new film id");
        }
        return newObject.toBuilder().id(keyHolder.getKey().intValue()).likesCount(0).build();
    }

    @Override
    public Film delete(int id) {
        final Film filmToRemove = getById(id);

        final String sql = "DELETE FROM \"film\" WHERE film_id = ?";
        jdbcTemplate.update(sql, id);

        return filmToRemove;
    }

    @Override
    public Collection<Film> getFilmsSortedByLikes() {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return null;
    }

    @Override
    public Film getById(int id) {
        return null;
    }
}
