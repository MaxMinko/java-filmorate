package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private Map<Integer, Film> films;
    FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        films = new HashMap<>();
        film = new Film(1, "TestFilm", "TestFilmDescription", LocalDate.of(2000, 12,
                12), Duration.ofSeconds(100));
    }

    @Test
    public void getAllFilms() {
        filmController.createFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilm() {
        filmController.createFilm(film);
        assertEquals(film, filmController.getAllFilms().get(1), "Задачи не совпадают.");
    }

    @Test
    public void updateFilm() {
        Film updatedFilm = new Film(1, "UpdatedTestFilm", "UpdatedTestFilmDescription", LocalDate.of(
                2000, 12, 12), Duration.ofSeconds(100));
        filmController.createFilm(film);
        filmController.updateFilm(updatedFilm);
        assertEquals(updatedFilm, filmController.getAllFilms().get(1), "Задачи не совпадают.");
    }

    @Test
    public void createFilmWithLargeDescription() {
        film.setDescription("TestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFil" +
                "mWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescripti" +
                "onTestFilmWithLargeDescription");
        filmController.createFilm(film);
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilmWithInvalidCreationDate() {
        film.setReleaseDate(LocalDate.of(1800, 12, 12));
        filmController.createFilm(film);
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilmWithNegativeDuration() {
        film.setDuration(Duration.ofSeconds(-100));
        filmController.createFilm(film);
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

}