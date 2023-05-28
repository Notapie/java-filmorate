package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> idToFilm;
    private final SortedSet<Film> orderedByLikesFilms;
    private final Map<Integer, Set<Integer>> userIdToLikedFilmsIds;
    private int idGenerator;

    public InMemoryFilmStorage() {
        this.idToFilm = new HashMap<>();
        this.orderedByLikesFilms = new TreeSet<>(new FilmByLikesComparator());
        this.userIdToLikedFilmsIds = new HashMap<>();
        this.idGenerator = 1;
    }

    @Override
    public Film createFilm(final Film film) {
        final Film createdFilm = film.toBuilder()
                .id(idGenerator++)
                .likesCount(0)
                .build();

        idToFilm.put(createdFilm.getId(), createdFilm);
        orderedByLikesFilms.add(createdFilm);

        return createdFilm;
    }

    @Override
    public Film updateFilm(final Film film) {
        final Film oldRecord = idToFilm.get(film.getId());

        if (oldRecord == null) {
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }

        idToFilm.put(film.getId(), film);
        orderedByLikesFilms.remove(oldRecord);
        orderedByLikesFilms.add(film);

        return film;
    }

    @Override
    public Film deleteFilm(final int filmId) {
        final Film oldRecord = idToFilm.remove(filmId);

        if (oldRecord == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }

        orderedByLikesFilms.remove(oldRecord);
        return oldRecord;
    }

    @Override
    public Film getFilmById(final int filmId) {
        return idToFilm.get(filmId);
    }

    @Override
    public Collection<Film> getFilmsSortedByLikes() {
        return orderedByLikesFilms;
    }

    private static long getLikeIndex(final Film film, final int likesCount) {
        return ((long) likesCount) << 32 | film.getId();
    }
}
