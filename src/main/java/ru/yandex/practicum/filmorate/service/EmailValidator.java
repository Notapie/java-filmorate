package ru.yandex.practicum.filmorate.service;

import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean validate(final String email) {
        final String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return patternMatches(email, regexPattern);
    }

    public static boolean patternMatches(final String emailAddress, final String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }
}
