package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private MpaStorage storage;

    // TODO: add logs and validation
    Mpa createNewMpa(final Mpa newObject) {
        return storage.create(newObject);
    }

    Mpa updateGenre(final Mpa newObject) {
        if (newObject.getId() == null) {
            throw new ValidationException("Mpa id must be not null");
        }
        return storage.update(newObject);
    }

    Collection<Mpa> getAllGenres() {
        return storage.getAll();
    }

    Mpa getGenreById(final int id) {
        return storage.getById(id);
    }
}
