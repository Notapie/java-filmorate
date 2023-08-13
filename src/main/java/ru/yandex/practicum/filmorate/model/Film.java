package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Integer likesCount;

    Mpa mpa;
    List<Genre> genres;
}
