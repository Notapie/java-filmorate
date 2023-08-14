package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidator {
    private final String EMAIL_REGEX_PATTERN;

    public EmailValidator(@Value("${email.regex}") final String emailRegex) {
        this.EMAIL_REGEX_PATTERN = emailRegex;
    }

    public boolean validate(final String email) {
        return patternMatches(email, EMAIL_REGEX_PATTERN);
    }

    public static boolean patternMatches(final String emailAddress, final String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }
}
