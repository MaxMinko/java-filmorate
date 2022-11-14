package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private Map<Integer, Film> films;
    FilmController filmController;
    Film film;
    InMemoryFilmStorage inMemoryFilmStorage;
    FilmService filmService;

    @BeforeEach
    void setUp() {
        inMemoryFilmStorage=new InMemoryFilmStorage();
        filmService =new FilmService(inMemoryFilmStorage);
        filmController = new FilmController(filmService);
        films = new HashMap<>();
        film = new Film(1, "TestFilm", "TestFilmDescription", LocalDate.of(2000, 12,
                12), 100);
    }

    @Test
    public void getAllFilms() {
        filmController.createFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilm() {
        filmController.createFilm(film);
        assertEquals(true, filmController.getAllFilms().contains(film), "Задачи не совпадают.");
    }

    @Test
    public void updateFilm() {
        Film updatedFilm = new Film(1, "UpdatedTestFilm", "UpdatedTestFilmDescription", LocalDate.of(
                2000, 12, 12), 100);
        filmController.createFilm(film);
        filmController.updateFilm(updatedFilm);
        assertEquals(true, filmController.getAllFilms().contains(updatedFilm), "Задачи не совпадают.");
    }

    @Test
    public void createFilmWithLargeDescription() {
        film.setDescription("TestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFil" +
                "mWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescriptionTestFilmWithLargeDescripti" +
                "onTestFilmWithLargeDescription");
        final ValidationException exception =assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.createFilm(film);
            }
        });
        assertEquals("Максимальная длина описания — 200 символов!",exception.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilmWithInvalidCreationDate() {
        film.setReleaseDate(LocalDate.of(1800, 12, 12));
        final ValidationException exception =assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.createFilm(film);
            }
        });
        assertEquals("Дата релиза не должна быть раньше 28 декабря 1895!",exception.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

    @Test
    public void createFilmWithNegativeDuration() {
        film.setDuration(-100);
        final ValidationException exception =assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                filmController.createFilm(film);
            }
        });
        assertEquals("Продолжительность фильма должна быть положительна!",exception.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Неверное количество фильмов.");
    }

}
