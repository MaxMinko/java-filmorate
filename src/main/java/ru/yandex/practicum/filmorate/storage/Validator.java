package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


public class Validator {
    InMemoryUserStorage inMemoryUserStorage;
    InMemoryFilmStorage inMemoryFilmStorage;
    private final static Logger log = LoggerFactory.getLogger(Validator.class);

    Validator(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    Validator(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void checkFilm(Film film) {
        if (inMemoryFilmStorage.films.containsKey(film.getId())) {
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

    public void checkUser(User user) {
        if (user.getLogin().contains("") && user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы!");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ - @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        if (inMemoryUserStorage.getUsers().containsKey(user.getId())) {
            log.error("Такой пользователь уже существует!");
            throw new ValidationException("Такой пользователь уже существует!");
        }
    }

}
