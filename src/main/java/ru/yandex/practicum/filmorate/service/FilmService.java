package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmDao filmDao;


    @Autowired
    public FilmService(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    public Collection<Film> getAllFilm() {
        return filmDao.getAllFilms();
    }

    public Optional<Film> getFilm(int id) {
        return filmDao.getFilm(id);
    }

    public Film addFilm(Film film) {
        return filmDao.addFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmDao.updateFilm(film);
    }

    public void addLike(int film_id, int id) {
        filmDao.addLike(film_id, id);
    }

    public void deleteLike(int filmId, int id) {
        filmDao.deleteLike(filmId, id);
    }

    public Collection<Film> getPopularFilm(int id) {
        return filmDao.getPopularFilm(id);
    }


    public List<Genre> getAllGenres() {
        return filmDao.getAllGenres();
    }

    public Genre getGenresById(int id) {
        return filmDao.getGenresById(id);
    }

    public MPA getMPAById(int id) {
        return filmDao.getMPAById(id);
    }

    public List<MPA> getAllMPA() {
        return filmDao.getAllMPA();
    }

}

