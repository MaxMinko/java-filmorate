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
    private int filmId=1;

    @GetMapping()
    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: " + films.size());
        return films.values();
    }

    @PostMapping()
    public Film createFilm(@RequestBody Film film) {
        try {
            if (films.containsKey(film.getId())) {
                log.error("Фильм уже был добавлен!, {}", film);
                throw new ValidationException("Фильм уже был добавлен!");
            } else if (film.getName().isBlank() || film.getName() == null) {
                log.error("Название не может быть пустым!, {}", film);
                throw new ValidationException("Название не может быть пустым!");
            } else if (film.getDescription().length() > 200) {
                log.error("Максимальная длина описания — 200 символов!, {}", film);
                throw new ValidationException("Максимальная длина описания — 200 символов!");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Дата релиза не должна быть раньше 28 декабря 1895!, {}", film);
                throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895!");
            } else if (film.getDuration().isNegative()) {
                log.error("Продолжительность фильма должна быть положительна!, {}", film);
                throw new ValidationException("Продолжительность фильма должна быть положительна!");
            } else {
                film.setId(filmId++);
                films.put(film.getId(), film);
                log.info("Добавлен новый фильм, {}", film);
                return film;
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return film;
        }
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        try {
            if (!films.containsKey(film.getId())) {
                log.error("Такого фильма не существует!, {}", film);
                throw new ValidationException("Такого фильма не существует!");
            } else {
                film.setId(filmId++);
                films.put(film.getId(), film);
                log.info("Фильм обновлен - , {}", film);
                return film;
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return film;
        }
    }


}
