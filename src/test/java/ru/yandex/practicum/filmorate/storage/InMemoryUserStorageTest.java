package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

class InMemoryUserStorageTest extends UserStorageTest {
    @Override
    protected UserStorage createNewStorage() {
        return new InMemoryUserStorage();
    }
}
