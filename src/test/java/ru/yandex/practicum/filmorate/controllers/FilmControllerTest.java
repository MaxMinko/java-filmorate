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
    public void getAllFilms() throws ValidationException {
        filmController.createFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilm() throws ValidationException {
        filmController.createFilm(film);
        assertEquals(true, filmController.getAllFilms().contains(film), "Задачи не совпадают.");
    }

    @Test
    public void updateFilm() throws ValidationException {
        Film updatedFilm = new Film(1, "UpdatedTestFilm", "UpdatedTestFilmDescription", LocalDate.of(
                2000, 12, 12), Duration.ofSeconds(100));
        filmController.createFilm(film);
        filmController.updateFilm(updatedFilm);
        assertEquals(true, filmController.getAllFilms().contains(updatedFilm), "Задачи не совпадают.");
    }

    @Test
    public void createFilmWithLargeDescription() throws ValidationException {
        film.setDescription("TestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFil" +
                "mWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescripti" +
                "onTestFilmWithLargeDescription");
        try {
            filmController.createFilm(film);
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilmWithInvalidCreationDate() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1800, 12, 12));
        try {
            filmController.createFilm(film);
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilmWithNegativeDuration() throws ValidationException {
        film.setDuration(Duration.ofSeconds(-100));
        try {
            filmController.createFilm(film);
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

}
