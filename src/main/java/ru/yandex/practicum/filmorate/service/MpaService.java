package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
public class MpaService {
    private final int maxNameLength;
    private final MpaStorage storage;

    public MpaService(MpaStorage storage, @Value("${max.mpa.name.length}") int maxNameLength) {
        this.maxNameLength = maxNameLength;
        this.storage = storage;
    }

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
        final Mpa mpa = storage.getById(id);
        if (mpa == null) {
            throw new NotFoundException("MPA with id " + id + " not found");
        }
        return mpa;
    }

    private void validate(final Mpa mpa) {
        if (!StringUtils.hasText(mpa.getName())) {
            throw  new ValidationException("MPA name cannot be blank or null");
        }
        if (mpa.getName().length() > maxNameLength) {
            throw new ValidationException("The length of the MPA name exceeds " + maxNameLength + " characters");
        }
    }
}
