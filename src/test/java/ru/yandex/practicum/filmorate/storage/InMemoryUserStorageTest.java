package ru.yandex.practicum.filmorate.storage;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest extends UserStorageTest {
    @Override
    protected UserStorage createNewStorage() {
        return new InMemoryUserStorage();
    }
}
