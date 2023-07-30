package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage extends StorageBase<Film> {
    boolean addLike(int userId, int filmId);

    boolean removeLike(int userId, int filmId);

    Collection<Film> getFilmsSortedByLikes();
}
