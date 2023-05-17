package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController controller;

    @BeforeEach
    public void updateController() {
        controller = new UserController();
    }

    @Test
    public void shouldAddNewUser() {
        final User user = controller.create(
                User.builder()
                        .email("email@email")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        );

        final User expectedUser = User.builder()
                .id(1)
                .email("email@email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1999, 7, 2))
                .build();

        assertEquals(expectedUser, user);
        assertEquals(expectedUser, controller.get().iterator().next());
    }

    @Test
    public void shouldUpdateUser() {
        controller.create(
                User.builder()
                        .email("email@email")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        );
        final User user = controller.update(
                User.builder()
                        .id(1)
                        .email("update@email")
                        .login("updatelogin")
                        .name("update name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        );

        final User expectedUser =
                User.builder()
                        .id(1)
                        .email("update@email")
                        .login("updatelogin")
                        .name("update name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build();


        assertEquals(expectedUser, user);
        assertEquals(1, controller.get().size());
        assertEquals(expectedUser, controller.get().iterator().next());
    }

    @Test
    public void shouldReturnAllUsers() {
        controller.create(
                User.builder()
                        .email("email@email")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        );
        controller.create(
                User.builder()
                        .email("second@email")
                        .login("secondlogin")
                        .name("second name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        );

        User[] expectedUsers = {
                User.builder()
                        .id(1)
                        .email("email@email")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build(),

                User.builder()
                        .id(2)
                        .email("second@email")
                        .login("secondlogin")
                        .name("second name")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        };

        assertArrayEquals(expectedUsers, controller.get().toArray());
    }

    @Test
    public void shouldSetLoginAsNameIfNameNullOrBlank() {
        final User user = controller.create(
                User.builder()
                        .email("email@email")
                        .login("commonlogin")
                        .birthday(LocalDate.of(1999, 7, 2))
                        .build()
        );
        assertEquals("commonlogin", user.getName());
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void shouldExceptionIfLoginNullBlankOrHaveSpaces() {
        ValidationException e = assertThrows(ValidationException.class, () -> controller.create(
                User.builder()
                        .login("")
                        .email("asd@asd")
                        .build()
        ));
        assertEquals("User login must be not null or blank.", e.getMessage());

        e = assertThrows(ValidationException.class, () -> controller.create(
                User.builder()
                        .email("asd@asd")
                        .build()
        ));
        assertEquals("User login must be not null or blank.", e.getMessage());

        e = assertThrows(ValidationException.class, () -> controller.create(
                User.builder()
                        .login("qwe qwe")
                        .email("asd@asd")
                        .build()
        ));
        assertEquals("Login cannot contain spaces.", e.getMessage());
    }

    @Test
    public void shouldExceptionIfBirthdayInTheFuture() {
        ValidationException e = assertThrows(ValidationException.class, () -> controller.create(
                User.builder()
                        .login("qweqwe")
                        .email("asd@asd")
                        .birthday(LocalDate.of(2077, 1, 1))
                        .build()
        ));
        assertEquals("The date of birth cannot be in the future.", e.getMessage());
    }
}
