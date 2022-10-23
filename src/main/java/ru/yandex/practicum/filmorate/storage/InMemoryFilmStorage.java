package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    public Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film addFilm(Film film) {
        checkFilm(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Такого фильма не существует!");
            throw new FilmNotFoundException("Такого фильма не существует!");
        } else {
            films.remove(film.getId());
            log.info("Фильм удален.");
            return film;
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Такого фильма не существует!");
            throw new FilmNotFoundException("Такого фильма не существует!");
        } else {
            films.put(film.getId(), film);
            log.info("Фильм обновлен");
            return film;
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: " + films.size());
        return films.values();
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        return films.get(id);
    }

    private void checkFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Фильм уже был добавлен!");
            throw new ValidationException("Фильм уже был добавлен!");
        }
        if (film.getName().isBlank() || film.getName() == null) {
            log.error("Название не может быть пустым!");
            throw new ValidationException("Название не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов!");
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не должна быть раньше 28 декабря 1895!");
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895!");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительна!");
            throw new ValidationException("Продолжительность фильма должна быть положительна!");
        }
    }
}
