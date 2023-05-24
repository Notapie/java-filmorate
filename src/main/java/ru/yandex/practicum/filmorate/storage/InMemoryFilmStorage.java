package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(Film film) {

    }

    @Override
    public Film getFilmById(int id) {
        return null;
    }

    @Override
    public Collection<Film> getFilmsSortedByLikes() {
        return null;
    }
}
