package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @GetMapping()
    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: " + films.size());
        return films.values();
    }

    @PostMapping()
    public Film createFilm(@RequestBody Film film)  {
        checkId(film);
        checkName(film);
        checkDescription(film);
        checkReleaseDate(film);
        checkDuration(film);
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм");
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Такого фильма не существует!");
            throw new ValidationException("Такого фильма не существует!");
        } else {
            films.put(film.getId(), film);
            log.info("Фильм обновлен");
            return film;
        }
    }

    private void checkId(Film film){
        if (films.containsKey(film.getId())) {
            log.error("Фильм уже был добавлен!");
            throw new ValidationException("Фильм уже был добавлен!");
        }
    }

    private void checkName(Film film)  {
        if (film.getName().isBlank() || film.getName() == null) {
            log.error("Название не может быть пустым!");
            throw new ValidationException("Название не может быть пустым!");
        }
    }

    private void checkDescription(Film film)  {
        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов!");
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        }
    }

    private void checkReleaseDate(Film film)  {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не должна быть раньше 28 декабря 1895!");
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895!");
        }
    }

    private void checkDuration(Film film) {
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительна!");
            throw new ValidationException("Продолжительность фильма должна быть положительна!");
        }
    }
}