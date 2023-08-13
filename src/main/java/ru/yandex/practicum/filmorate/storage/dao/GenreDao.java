package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre update(Genre newObject) {
        final String sql = "UPDATE \"genre\" SET name = ? WHERE id = ?";

        boolean isUpdated = jdbcTemplate.update(sql, newObject.getName(), newObject.getId()) > 0;

        if (!isUpdated) {
            throw new NotFoundException("Genre with id " + newObject.getId() + " is not found");
        }

        return getById(newObject.getId());
    }

    @Override
    public Genre create(Genre newObject) {
        final String sql = "INSERT INTO \"genre\" (name) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newObject.getName());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Error getting the new genre id");
        }

        return newObject.toBuilder().id(keyHolder.getKey().intValue()).build();
    }

    @Override
    public Genre delete(int id) {
        final Genre genreToRemove = getById(id);

        final String sql = "DELETE FROM \"genre\" WHERE id = ?";
        jdbcTemplate.update(sql, id);

        return genreToRemove;
    }

    @Override
    public Collection<Genre> getAll() {
        final String sql = "SELECT * FROM \"genre\"";
        return jdbcTemplate.query(sql, (rs, rn) -> makeGenre(rs));
    }

    @Override
    public Genre getById(int id) {
        final String sql = "SELECT * FROM \"genre\" WHERE id = ?";
        List<Genre> result = jdbcTemplate.query(sql, (rs, rn) -> makeGenre(rs), id);

        if (result.isEmpty()) {
            throw new NotFoundException("Genre with id " + id + " is not found");
        }

        return result.get(0);
    }

    @Override
    public void setFilmGenres(final int filmId, final Collection<Genre> genres) {
        final String sql = "DELETE FROM \"film_genre\" WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);

        if (genres == null || genres.isEmpty()) {
            return;
        }

        final Set<Integer> uniqGenreIds = new HashSet<>();
        for (final Genre genre : genres) {
            uniqGenreIds.add(genre.getId());
        }

        StringBuilder addGenresSql = new StringBuilder("INSERT INTO \"film_genre\" (film_id, genre_id) VALUES ");
        List<Integer> params = new ArrayList<>();

        int isFirst = 0;
        for (final int genre : uniqGenreIds) {
            if (isFirst++ > 0) {
                addGenresSql.append(", ");
            }

            addGenresSql.append("(?, ?)");
            params.add(filmId);
            params.add(genre);
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(addGenresSql.toString());
            for (int i = 0; i < params.size(); ++i) {
                ps.setInt(i + 1, params.get(i));
            }
            return ps;
        });
    }

    @Override
    public Collection<Genre> getFilmGenres(int filmId) {
        final String sql = "SELECT g.* FROM \"film_genre\" AS fg " +
                "RIGHT JOIN \"genre\" AS g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rn) -> makeGenre(rs), filmId);
    }

    private Genre makeGenre(final ResultSet resultSet) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
