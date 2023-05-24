package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    void linkAsFriends(int firstUserId, int secondUserId);

    void unlinkFriends(int firstUserId, int secondUserId);

    void deleteFilm(Film film);

    Film getFilmById(int id);

    Collection<Film> getFilmsSortedByLikes();
}
