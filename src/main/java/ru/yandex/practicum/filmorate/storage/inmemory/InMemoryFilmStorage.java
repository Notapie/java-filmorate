package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> idToFilm;
    private final SortedSet<Film> orderedByLikesFilms;
    private final Map<Integer, Set<Integer>> userIdToLikedFilmsIds;
    private final Map<Integer, Set<Integer>> filmToUserLiked;
    private int idGenerator;

    public InMemoryFilmStorage() {
        this.idToFilm = new HashMap<>();
        this.orderedByLikesFilms = new TreeSet<>(new FilmByLikesComparator().reversed());
        this.userIdToLikedFilmsIds = new HashMap<>();
        this.filmToUserLiked = new HashMap<>();
        this.idGenerator = 1;
    }

    @Override
    public Film create(final Film film) {
        final Film createdFilm = film.toBuilder()
                .id(idGenerator++)
                .likesCount(0)
                .build();

        idToFilm.put(createdFilm.getId(), createdFilm);
        orderedByLikesFilms.add(createdFilm);

        return createdFilm;
    }

    @Override
    public Film update(final Film film) {
        final Film oldRecord = idToFilm.get(film.getId());

        if (oldRecord == null) {
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }

        final Film newFilm = film.toBuilder().likesCount(oldRecord.getLikesCount()).build();
        idToFilm.put(newFilm.getId(), newFilm);
        orderedByLikesFilms.remove(oldRecord);
        orderedByLikesFilms.add(newFilm);

        return newFilm;
    }

    @Override
    public void addLike(final int userId, final int filmId) {
        Film film = idToFilm.get(filmId);

        if (film == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }

        final Set<Integer> likedFilms = userIdToLikedFilmsIds.computeIfAbsent(userId, k -> new HashSet<>());
        final Set<Integer> usersLiked = filmToUserLiked.computeIfAbsent(filmId, k -> new HashSet<>());

        if (likedFilms.contains(filmId)) {
            throw new AlreadyExistsException("The movie id " + filmId + " is already in favorites");
        }

        likedFilms.add(filmId);
        usersLiked.add(userId);
        film = film.toBuilder().likesCount(film.getLikesCount() + 1).build();
        update(film);
    }

    @Override
    public void removeLike(int userId, int filmId) {
        final Set<Integer> likedFilms = userIdToLikedFilmsIds.get(userId);
        if (likedFilms == null || !likedFilms.contains(filmId)) {
            throw new NotFoundException("Film with id " + filmId + " not found in favorites");
        }
        likedFilms.remove(filmId);

        final Set<Integer> usersLiked = filmToUserLiked.get(filmId);
        usersLiked.remove(filmId);

        Film film = idToFilm.get(filmId);
        if (film != null) {
            final int newLikesCount = Integer.max(film.getLikesCount() - 1, 0);
            film = film.toBuilder().likesCount(newLikesCount).build();
            update(film);
        }
    }

    @Override
    public Film delete(final int filmId) {
        final Film oldRecord = idToFilm.remove(filmId);

        if (oldRecord == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }

        orderedByLikesFilms.remove(oldRecord);
        return oldRecord;
    }

    @Override
    public Film getById(final int filmId) {
        return idToFilm.get(filmId);
    }

    @Override
    public Collection<Film> getFilmsSortedByLikes(final int limit) {
        final Collection<Film> result = new ArrayList<>();
        Iterator<Film> it = orderedByLikesFilms.iterator();
        for (int i = 0; i < limit && it.hasNext(); i++) {
            result.add(it.next());
        }

        return result;
    }

    @Override
    public Collection<Film> getAll() {
        return idToFilm.values();
    }

    private static long getLikeIndex(final Film film, final int likesCount) {
        return ((long) likesCount) << 32 | film.getId();
    }
}
