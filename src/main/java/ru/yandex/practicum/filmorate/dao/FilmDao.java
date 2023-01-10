package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Film addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void addLike(int filmId, int id);

    void deleteLike(int filmId, int id);

    Collection<Film> getPopularFilm(int id);

    Optional<Film> getFilm(int id);

    Genre getGenresById(int id);

    List<Genre> getAllGenres();

    MPA getMPAById(int id);

    List<MPA> getAllMPA();

    Collection<Film> getAllFilms();
}
