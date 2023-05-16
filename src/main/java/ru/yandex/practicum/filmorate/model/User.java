package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class User {
    int id;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String login;

    String name;

    LocalDate birthday;
}
