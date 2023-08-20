package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidator {
    private final String emailRegexPattern;

    public EmailValidator(@Value("${email.regex}") final String emailRegex) {
        this.emailRegexPattern = emailRegex;
    }

    public boolean validate(final String email) {
        return patternMatches(email, emailRegexPattern);
    }

    public static boolean patternMatches(final String emailAddress, final String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }
}
