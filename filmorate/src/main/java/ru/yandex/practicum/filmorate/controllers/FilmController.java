package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap();

    @GetMapping("/films")
    public Map<Integer, Film> getAllFilms() {
        log.info("Текущее количество фильмов: "+films.size());
        return films;
    }

    @PostMapping("/film")
    public Film createFilm(@RequestBody Film film) {
        validationFilm(film);
        log.info(film.toString());
        return film;
    }

    @PutMapping("/film")
    public Film updateFilm(@RequestBody Film film) {
        validationFilm(film);
        log.info(film.toString());
        return film;
    }

    public void validationFilm(Film film) {
        try {
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Количество символов в описании больше 200.");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза не может быть раньше чем 28.12.1895.");
            }
            if (film.getDuration().isNegative()) {
                throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
            }
            films.put(film.getId(), film);
        } catch (ValidationException exception) {
            log.info(exception.getMessage());
        }
    }
}
