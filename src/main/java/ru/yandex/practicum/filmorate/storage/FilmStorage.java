package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film createFilm(final Film film);

    Film updateFilm(final Film film);

    void deleteFilm(final Film film);

    Film getFilmById(final int id);

    Collection<Film> getFilmsSortedByLikes();

}
