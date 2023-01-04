package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


public class Validator {

    UserDaoImpl userDao;
    FilmDaoImpl filmDao;
    private final static Logger log = LoggerFactory.getLogger(Validator.class);


    public Validator(UserDaoImpl userDao){
        this.userDao=userDao;
    }
    public Validator(FilmDaoImpl filmDao){
        this.filmDao=filmDao;
    }

    public void checkUserForDataBase(User user, JdbcTemplate jdbcTemplate){
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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id=?", user.getId());
        if(userRows.wasNull()){
            log.error("Такой пользователь уже существует!");
            throw new ValidationException("Такой пользователь уже существует!");
        }

    }


    public void checkFilmForDataBase(Film film,JdbcTemplate jdbcTemplate){

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
        if(film.getMpa()==null){
            log.error("Жанр фильма не указан!");
            throw new ValidationException("Жанр фильма не указан!");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from films where id=?", film.getId());
        if (userRows.wasNull()) {
            log.error("Фильм уже был добавлен!");
            throw new ValidationException("Фильм уже был добавлен!");
        }
    }

}
