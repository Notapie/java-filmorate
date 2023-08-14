package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;

    // TODO: add validation
    public Genre createNewGenre(final Genre newObject) {
        final Genre result = storage.create(newObject);
        log.debug("Added new genre: " + result);
        return result;
    }

    public Genre updateGenre(final Genre newObject) {
        if (newObject.getId() == null) {
            throw new ValidationException("Genre id must be not null");
        }

        final Genre result = storage.update(newObject);
        log.debug("Updated genre: " + result);
        return result;
    }

    public Collection<Genre> getAllGenres() {
        log.debug("Getting all genres");
        return storage.getAll();
    }

    public Genre getGenreById(final int id) {
        log.debug("Getting genre with id: " + id);
        return storage.getById(id);
    }
}
