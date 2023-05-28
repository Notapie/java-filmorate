package ru.yandex.practicum.filmorate.storage.inmemory;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmByLikesComparator implements Comparator<Film> {
    @Override
    public int compare(final Film lhs, final Film rhs) {
        final int byLikes = lhs.getLikesCount().compareTo(rhs.getLikesCount());

        if (byLikes != 0) {
            return byLikes;
        }
        return lhs.getId().compareTo(rhs.getId());
    }
}
