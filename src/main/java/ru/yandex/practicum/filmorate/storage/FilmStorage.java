package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    boolean addLike(int userId, int filmId);

    boolean removeLike(int userId, int filmId);

    Film deleteFilm(int filmId);

    Film getFilmById(int filmId);

    Collection<Film> getFilmsSortedByLikes();

    Collection<Film> getAll();
}
