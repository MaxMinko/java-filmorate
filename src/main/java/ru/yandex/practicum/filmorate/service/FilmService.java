package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;


    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(int filmId, int id) {
        inMemoryFilmStorage.getFilm(filmId).getLikes().add(id);
    }

    public void deleteLike(int filmId, int id) {
        if (inMemoryFilmStorage.getFilm(filmId).getLikes().contains(id)) {
            inMemoryFilmStorage.getFilm(filmId).getLikes().remove(id);
        } else {
            throw new LikeNotFoundException("Лайк не найден.");
        }
    }

    public Film getFilm(int id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Collection<Film> getAllFilm() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Collection<Film> getPopularFilm(int id) {
        List<Film> popularFilms = new ArrayList<>();
        for (Film film : inMemoryFilmStorage.getAllFilms()) {
            popularFilms.add(film);
        }
        Collections.sort(popularFilms);
        while (popularFilms.size() > id) {
            popularFilms.remove(0);
        }
        return popularFilms;
    }
}
