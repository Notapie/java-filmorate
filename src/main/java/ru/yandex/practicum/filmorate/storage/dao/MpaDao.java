package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDao implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa update(Mpa newObject) {
        final String sql = "UPDATE \"mpa\" SET name = ? WHERE id = ?";

        boolean isUpdated = jdbcTemplate.update(sql, newObject.getName(), newObject.getId()) > 0;

        if (!isUpdated) {
            throw new NotFoundException("Mpa with id " + newObject.getId() + " is not found");
        }

        return getById(newObject.getId());
    }

    @Override
    public Mpa create(Mpa newObject) {
        final String sql = "INSERT INTO \"mpa\" (name) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newObject.getName());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new RuntimeException("Error getting the new mpa id");
        }

        return newObject.toBuilder().id(keyHolder.getKey().intValue()).build();
    }

    @Override
    public Mpa delete(int id) {
        final Mpa mpaToRemove = getById(id);

        final String sql = "DELETE FROM \"mpa\" WHERE id = ?";
        jdbcTemplate.update(sql, id);

        return mpaToRemove;
    }

    @Override
    public Collection<Mpa> getAll() {
        final String sql = "SELECT * FROM \"mpa\"";
        return jdbcTemplate.query(sql, (rs, rn) -> makeMpa(rs));
    }

    @Override
    public Mpa getById(int id) {
        final String sql = "SELECT * FROM \"mpa\" WHERE id = ?";
        List<Mpa> result = jdbcTemplate.query(sql, (rs, rn) -> makeMpa(rs), id);

        if (result.isEmpty()) {
            throw new NotFoundException("Mpa with id " + id + " is not found");
        }

        return result.get(0);
    }

    private Mpa makeMpa(final ResultSet resultSet) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
