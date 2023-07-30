package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Genre update(Genre newGenre);

    Genre createNew(Genre newGenre);

    Genre delete(int genreId);

    Collection<Genre> getAll();

    Genre getById(int genreId);
}
