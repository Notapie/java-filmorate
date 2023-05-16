package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Film {
    Integer id;

    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @NotNull
    LocalDate releaseDate;

    @NotNull
    Duration duration;
}
