package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private GenreStorage storage;

    // TODO: add logs and validation
    Genre createNewGenre(final Genre newObject) {
        return storage.create(newObject);
    }

    Genre updateGenre(final Genre newObject) {
        if (newObject.getId() == null) {
            throw new ValidationException("Genre id must be not null");
        }
        return storage.update(newObject);
    }

    Collection<Genre> getAllGenres() {
        return storage.getAll();
    }

    Genre getGenreById(final int id) {
        return storage.getById(id);
    }
}
