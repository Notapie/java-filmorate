package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(int filmId);

    Film getFilmById(int filmId);

    Collection<Film> getFilmsSortedByLikes();
}
