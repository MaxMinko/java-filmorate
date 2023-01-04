package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDaoImpl implements FilmDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final Validator validator = new Validator(this);

    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        validator.checkFilmForDataBase(film, jdbcTemplate);
        String sqlQuery = "insert into films (name, description,releaseDate,duration)" + "values(?,?,?,?)";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select id from films GROUP BY(id) ORDER BY id DESC LIMIT 1;");
        if (userRows.next()) {
            for (Genre genre : film.getGenres()) {
                String sqlQuery2 = "insert into film_genre (film_id, genre_id)" + "values(?,?)";
                jdbcTemplate.update(sqlQuery2, userRows.getInt("id"), genre.getId());
            }
            String sqlQuery3 = "insert into mpa (mpa_id,film_id)" + "values(?,?)";
            jdbcTemplate.update(sqlQuery3, film.getMpa().getId(), userRows.getInt("ID"));
            film.setId(userRows.getInt("ID"));
        }
        log.info("Фильм с идентификатором " + film.getId() + " добавлен.");
        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from films where id=?", film.getId());
        if (!userRows.next()) {
            throw new FilmNotFoundException("Фильм " + film.getId() + " не найден.");
        }
        String sqlQuery = "update  films set" + " name=?, description=?,releaseDate=?,duration=?" + "where id=?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("select * from MPA where FILM_ID=?", film.getId());
        if (userRows2.next()) {
            String sqlQuery1 = "update  MPA set" + " MPA_ID=?" + "where FILM_ID=?";
            jdbcTemplate.update(sqlQuery1, film.getMpa().getId(), film.getId());
        }
        String sqlQuery2 = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery2, film.getId());
        if (film.getGenres().size() != 0) {
            List genres_id = new ArrayList<>();
            for (Genre genre : film.getGenres()) {
                if (!genres_id.contains(genre.getId())) {
                    String sqlQuery3 = "insert into film_genre (film_id, genre_id)" + "values(?,?)";
                    jdbcTemplate.update(sqlQuery3, film.getId(), genre.getId());
                }
            }
        }
        return getFilm(film.getId());
    }

    @Override
    public void addLike(int filmId, int id) {
        String sqlQuery = "insert into likes (film_id,user_id)" + "values(?,?)";
        jdbcTemplate.update(sqlQuery, filmId, id);
        log.info("Лайк фильму "+filmId+" пользователя "+id+" добавлен.");
    }

    @Override
    public void deleteLike(int filmId, int id) {
        SqlRowSet userRows3 = jdbcTemplate.queryForRowSet("select * from LIKES  where film_id=? and user_id=?", filmId, id);
        if (!userRows3.next()) {
            throw new FilmNotFoundException("Лайк пользователя " + id + " фильму " + filmId + " для удаления не найден.");
        }
        String sqlQuery = "delete from likes where film_id = ? and user_id=?";
        jdbcTemplate.update(sqlQuery, filmId, id);
        log.info("Лайк фильму "+filmId+" пользователя "+id+" удален.");
    }

    @Override
    public Collection<Film> getPopularFilm(int id) {
        List<Film> popularFilm = new ArrayList<>();
        SqlRowSet userRows;
        if (id == 10) {
            userRows = jdbcTemplate.queryForRowSet("select COUNT(USER_ID),film_id from LIKES GROUP BY(FILM_ID)   ORDER BY COUNT(USER_ID) DESC LIMIT 10 ");
        } else {
            userRows = jdbcTemplate.queryForRowSet("select COUNT(USER_ID),film_id from LIKES GROUP BY(FILM_ID)  ORDER BY COUNT(USER_ID) DESC LIMIT ?", id);
        }
        if (userRows.next()) {
            SqlRowSet popularsFilm = jdbcTemplate.queryForRowSet("select * from films where id=?", userRows.getInt("film_id"));
            if (popularsFilm.next()) {
                SqlRowSet popularFilmMPA = jdbcTemplate.queryForRowSet("select mpa_id from MPA where film_id=?", userRows.getInt("film_id"));
                if (popularFilmMPA.next()) {
                    MPA mpa = new MPA(popularFilmMPA.getInt("MPA_ID"));
                    Film film = new Film(
                            popularsFilm.getInt("id"),
                            popularsFilm.getString("name"),
                            popularsFilm.getString("description"),
                            popularsFilm.getDate("releaseDate").toLocalDate(),
                            popularsFilm.getInt("duration"),
                            mpa
                    );
                    popularFilm.add(film);
                }
            }
        } else {
            SqlRowSet popularFilmNo = jdbcTemplate.queryForRowSet("select * from films GROUP BY(id) ORDER BY id;");
            while (popularFilmNo.next()) {
                SqlRowSet popularFilmMPA1 = jdbcTemplate.queryForRowSet("select mpa_id from MPA where film_id=?", popularFilmNo.getInt("id"));
                if (popularFilmMPA1.next()) {
                    MPA mpa = new MPA(popularFilmMPA1.getInt("MPA_ID"));
                    Film film = new Film(
                            popularFilmNo.getInt("id"),
                            popularFilmNo.getString("name"),
                            popularFilmNo.getString("description"),
                            popularFilmNo.getDate("releaseDate").toLocalDate(),
                            popularFilmNo.getInt("duration"),
                            mpa
                    );
                    popularFilm.add(film);
                }
            }
        }
        return popularFilm;
    }

    @Override
    public Optional<Film> getFilm(int id) {

        SqlRowSet userRows1 = jdbcTemplate.queryForRowSet("select MPA_ID from MPA where FILM_ID = ?", id);
        MPA mpa = null;
        if (userRows1.next()) {
            mpa = new MPA(userRows1.getInt("MPA_ID"));
            SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("select NAME_MPA from MPA_NAME where MPA_ID = ?", mpa.getId());
            if (userRows2.next()) {
                mpa.setName(userRows2.getString("NAME_MPA"));
            }
        }

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);
        if (userRows.next()) {
            Film film;
            film = new Film(userRows.getInt("id"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate").toLocalDate(),
                    userRows.getInt("duration"),
                    mpa
            );

            SqlRowSet filmLikes = jdbcTemplate.queryForRowSet("select user_id from likes where film_id = ?", id);
            while (filmLikes.next()) {
                film.getLikes().add(filmLikes.getInt("user_id"));
            }
            SqlRowSet filmGenres = jdbcTemplate.queryForRowSet("select genre_id from film_genre where film_id = ?", id);
            while (filmGenres.next()) {
                SqlRowSet filmGenre = jdbcTemplate.queryForRowSet("select genre_name from genres where genre_id = ?", filmGenres.getInt("genre_id"));
                while (filmGenre.next()) {
                    Genre genre = new Genre(filmGenres.getInt("GENRE_ID"));
                    genre.setName(filmGenre.getString("GENRE_NAME"));
                    film.getGenres().add(genre);
                }
            }
            log.info("Найден фильм: "+ film.getId());
            return Optional.of(film);
        } else {
            throw new FilmNotFoundException("Фильм с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from genres");
        while (userRows.next()) {
            Genre genre = new Genre(userRows.getInt("GENRE_ID"), userRows.getString("GENRE_NAME"));
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public MPA getMPAById(int id) {
        if ((id - 1 > 5) || (id - 1 < 0)) {
            throw new FilmNotFoundException("MPA с id = " + id + " не найден.");
        }
        return getAllMPA().get(id - 1);
    }

    @Override
    public List<MPA> getAllMPA() {
        List<MPA> mpas = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from MPA_NAME");
        while (userRows.next()) {
            MPA mpa = new MPA(userRows.getInt("MPA_ID"), userRows.getString("NAME_MPA"));
            mpas.add(mpa);
        }
        return mpas;
    }

    @Override
    public Genre getGenresById(int id) {
        if ((id - 1 > 6) || (id - 1 < 0)) {
            throw new FilmNotFoundException("Жанр с id = " + id + " не найден.");
        }
        return getAllGenres().get(id - 1);
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet userRows3 = jdbcTemplate.queryForRowSet("select id from FILMS");
        while (userRows3.next()) {
            films.add(getFilm(userRows3.getInt("id")).get());
        }
        return films;
    }


}

