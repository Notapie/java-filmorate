package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private FilmController controller;

    @BeforeEach
    public void updateController() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    public void webTest() throws Exception {
        Film film = Film.builder()
                .name("film")
                .description("test film")
                .releaseDate(LocalDate.of(1999, 7, 2))
                .duration(200).build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film))
        ).andExpect(status().isOk()).andExpect(content().json(
                objectMapper.writeValueAsString(film.toBuilder().id(1).build())
        ));
    }

    @Test
    public void shouldAddNewFilm() {
        final Film film = controller.create(
                Film.builder()
                        .name("film")
                        .description("film for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(190)
                        .build()
        );

        final Film expectedFilm = Film.builder()
                .id(1)
                .name("film")
                .description("film for tests")
                .releaseDate(LocalDate.of(1999, 7, 2))
                .duration(190)
                .build();

        assertEquals(expectedFilm, film);
        assertEquals(expectedFilm, controller.get().iterator().next());
    }

    @Test
    public void shouldUpdateFilm() {
        controller.create(
                Film.builder()
                        .name("film")
                        .description("film for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(190)
                        .build()
        );
        final Film film = controller.update(
                Film.builder()
                        .id(1)
                        .name("updated")
                        .description("updated film for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(190)
                        .build()
        );
        final Film expectedFilm = Film.builder()
                .id(1)
                .name("updated")
                .description("updated film for tests")
                .releaseDate(LocalDate.of(1999, 7, 2))
                .duration(190)
                .build();

        assertEquals(expectedFilm, film);
        assertEquals(1, controller.get().size());
        assertEquals(expectedFilm, controller.get().iterator().next());
    }

    @Test
    public void shouldReturnAllFilms() {
        controller.create(
                Film.builder()
                        .name("film")
                        .description("film for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(190)
                        .build()
        );
        controller.create(
                Film.builder()
                        .name("film2")
                        .description("film2 for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(200)
                        .build()
        );

        Film[] expected = {
                Film.builder()
                        .id(1)
                        .name("film")
                        .description("film for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(190)
                        .build(),
                Film.builder()
                        .id(2)
                        .name("film2")
                        .description("film2 for tests")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(200)
                        .build(),

        };

        assertArrayEquals(expected, controller.get().toArray());
    }

    @Test
    public void shouldExceptionIfNewFilmNameIsNullOrBlank() {
        ValidationException e = assertThrows(ValidationException.class, () -> controller.create(
                Film.builder()
                        .description("desc")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(21)
                        .build()
        ));

        assertEquals("film name must be not null or blank.", e.getMessage());
    }

    @Test
    public void shouldExceptionIfNewFilmDescSizeMoreThan200() {
        ValidationException e = assertThrows(ValidationException.class, () -> controller.create(
                Film.builder()
                        .name("film")
                        .description("desc".repeat(51))
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(21)
                        .build()
        ));

        assertEquals("film desc must be less than 200.", e.getMessage());
    }

    @Test
    public void shouldExceptionIfNewFilmReleaseDateIsBefore1895() {
        ValidationException e = assertThrows(ValidationException.class, () -> controller.create(
                Film.builder()
                        .name("film")
                        .description("desc")
                        .releaseDate(LocalDate.of(1800, 7, 2))
                        .duration(21)
                        .build()
        ));

        assertEquals("Invalid film release date.", e.getMessage());
    }

    @Test
    public void shouldExceptionIfNewFilmDurationIsNegative() {
        ValidationException e = assertThrows(ValidationException.class, () -> controller.create(
                Film.builder()
                        .name("film")
                        .description("desc")
                        .releaseDate(LocalDate.of(1999, 7, 2))
                        .duration(-23)
                        .build()
        ));

        assertEquals("Invalid film duration. Duration must be positive.", e.getMessage());
    }
}
