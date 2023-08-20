package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailValidatorTest {
    @Autowired
    private EmailValidator emailValidator;

    @Test
    void shouldReturnTrue() {
        boolean result = emailValidator.validate("example@domain.com");
        assertTrue(result);

        result = emailValidator.validate("example@do.ru");
        assertTrue(result);
    }

    @Test
    void shouldReturnFalse() {
        boolean result = emailValidator.validate("example");
        assertFalse(result);

        result = emailValidator.validate("example@");
        assertFalse(result);

        result = emailValidator.validate("");
        assertFalse(result);
    }
}
