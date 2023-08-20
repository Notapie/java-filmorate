package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends StorageBase<Film> {
    void addLike(int userId, int filmId);

    void removeLike(int userId, int filmId);

    Collection<Film> getFilmsSortedByLikes(int limit);
}
