package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

abstract class UserStorageTest {
    private UserStorage storage;

    abstract protected UserStorage createNewStorage();

    @BeforeEach
    public void updateStorage() {
        storage = createNewStorage();
    }

    @Test
    public void shouldAddNewUser() {
        User user = User.builder()
                .name("test")
                .email("test email")
                .login("test login")
                .birthday(LocalDate.of(1999, 7, 2))
                .build();

        storage.createUser(user);

        user = user.toBuilder().id(1).build();
        assertEquals(user, storage.getUserById(1));
    }

}
