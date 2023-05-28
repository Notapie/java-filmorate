package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
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
        return null;
    }

    @Override
    public void deleteFilm(final Film film) {

    }

    @Override
    public Film getFilmById(final int id) {
        return null;
    }

    @Override
    public Collection<Film> getFilmsSortedByLikes() {
        return null;
    }

    private static long getLikeIndex(final Film film, final int likesCount) {
        return ((long) likesCount) << 32 | film.getId();
    }
}
