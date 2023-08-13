package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage extends StorageBase<Genre> {
    void setFilmGenres(int filmId, Collection<Genre> genres);

    Collection<Genre> getFilmGenres(int filmId);
}
