package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage storage;

    public Mpa createNewMpa(final Mpa newObject) {
        validate(newObject);
        final Mpa result = storage.create(newObject);
        log.debug("Created new MPA: " + result);
        return result;
    }

    public Mpa updateMpa(final Mpa newObject) {
        if (newObject.getId() == null) {
            throw new ValidationException("MPA id must be not null");
        }
        validate(newObject);

        final Mpa result = storage.update(newObject);
        log.debug("Updated MPA: " + result);
        return result;
    }

    public Collection<Mpa> getAllMpa() {
        log.debug("Getting all MPA");
        return storage.getAll();
    }

    public Mpa getMpaById(final int id) {
        log.debug("Getting MPA by id " + id);
        return storage.getById(id);
    }

    private void validate(final Mpa mpa) {
        final int maxNameLength = 16;

        if (!StringUtils.hasText(mpa.getName())) {
            throw  new ValidationException("MPA name cannot be blank or null");
        }
        if (mpa.getName().length() > maxNameLength) {
            throw new ValidationException("The length of the MPA name exceeds " + maxNameLength + " characters");
        }
    }
}
