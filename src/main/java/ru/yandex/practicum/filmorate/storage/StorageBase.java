package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface StorageBase<T> {
    T update(T newObject);

    T create(T newObject);

    T delete(int id);

    Collection<T> getAll();

    T getById(int id);
}
