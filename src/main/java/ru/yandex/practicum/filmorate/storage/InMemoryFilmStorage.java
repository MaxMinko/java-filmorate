package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    public Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;
    private Validator validator = new Validator(this);

    @Override
    public Film addFilm(Film film) {
        validator.checkFilm(film);
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


}
