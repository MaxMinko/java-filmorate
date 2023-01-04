package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class IntegrationTest {

    private final UserDaoImpl userDao;
    private final FilmDaoImpl filmDao;
    User testUser = new User(1, "testMail@ru", "testLogin", "testName", LocalDate.of(2000,
            12, 12));
    Film testFilm = new Film(1, "testFilm", "testDescription", LocalDate.of(1990, 12,
            12), 100, new MPA(1));

    @Test
    @Order(1)
    public void testFindUserById() {
        userDao.addUser(testUser);
        assertEquals(testUser, userDao.findUserById(1).get());
    }

    @Test
    @Order(2)
    public void testGetAllUsers() {
        userDao.addUser(testUser);
        assertEquals(1, userDao.getAllUsers().size());
    }

    @Test
    @Order(3)
    public void testAddFriend() {
        userDao.addUser(testUser);
        User testUserFriend = new User(2, "testFriendEmail@", "testFriendLogin", "testFriendLogin", LocalDate.of(2000, 1, 1));
        userDao.addUser(testUserFriend);
        userDao.addFriend(testUser.getId(), testUserFriend.getId());
        assertEquals(testUserFriend, userDao.getFriends(testUser.getId()).get(0).get());
    }

    @Test
    @Order(4)
    public void testUpdateUser() {
        userDao.addUser(testUser);
        User testUserUpdate = new User(1, "updateTestMail@ru", "updateTestLogin", "updateTestName",
                LocalDate.of(2000, 12, 12));
        userDao.updateUser(testUserUpdate);
        assertEquals(testUserUpdate, userDao.findUserById(1).get());
    }

    @Test
    @Order(5)
    public void testRemoveFriend() {
        userDao.addUser(testUser);
        User testUserFriend = new User(2, "testFriendEmail@", "testFriendLogin", "testFriendLogin",
                LocalDate.of(2000, 1, 1));
        userDao.addUser(testUserFriend);
        userDao.addFriend(testUser.getId(), testUserFriend.getId());
        userDao.removeFriend(testUser.getId(), testUserFriend.getId());
        assertEquals(0, testUser.getFriends().size());
    }

    @Test
    @Order(6)
    public void testGetCommonFriend() {
        userDao.addUser(testUser);
        User testUserFriend = new User(2, "testFriendEmail@", "testFriendLogin", "testFriendLogin",
                LocalDate.of(2000, 1, 1));
        userDao.addUser(testUserFriend);
        User testUserCommonFriend = new User(3, "testCommonFriendEmail@", "testCommonFriendLogin",
                "testCommonFriendLogin",
                LocalDate.of(2000, 1, 1));
        userDao.addUser(testUserCommonFriend);
        userDao.addFriend(testUser.getId(), testUserCommonFriend.getId());
        userDao.addFriend(testUserFriend.getId(), testUserCommonFriend.getId());
        assertEquals(1, userDao.getCommonFriend(testUser.getId(), testUserFriend.getId()).size());
    }

    @Test
    @Order(7)
    public void testAddFilm() {
        filmDao.addFilm(testFilm);
        assertEquals(testFilm, filmDao.getFilm(1).get());
    }

    @Test
    @Order(8)
    public void testUpdateFilm() {
        filmDao.addFilm(testFilm);
        Film testUpdateFilm = new Film(1, "updateName", "updateDescription", LocalDate.of(1990, 12,
                12), 200, new MPA(1, "G"));
        filmDao.updateFilm(testUpdateFilm);
        assertEquals(testUpdateFilm, filmDao.getFilm(1).get());
    }

    @Test
    @Order(9)
    public void testAddLike() {
        filmDao.addFilm(testFilm);
        userDao.addUser(testUser);
        filmDao.addLike(testFilm.getId(), testUser.getId());
        assertEquals(1, filmDao.getFilm(1).get().getLikes().size());
    }

    @Test
    @Order(10)
    public void testDeleteLike() {
        filmDao.addFilm(testFilm);
        userDao.addUser(testUser);
        filmDao.addLike(testFilm.getId(), testUser.getId());
        assertEquals(1, filmDao.getFilm(1).get().getLikes().size());
        filmDao.deleteLike(testFilm.getId(), testUser.getId());
        assertEquals(0, filmDao.getFilm(1).get().getLikes().size());
    }

    @Test
    @Order(11)
    public void testGetPopularFilm() {
        filmDao.addFilm(testFilm);
        userDao.addUser(testUser);
        filmDao.addLike(testFilm.getId(), testUser.getId());
        assertEquals(1, filmDao.getPopularFilm(1).size());
    }

    @Test
    @Order(12)
    public void testGetAllGenres() {
        assertEquals(6, filmDao.getAllGenres().size());
    }

    @Test
    @Order(13)
    public void testGetMpaById() {
        assertEquals(1, filmDao.getMPAById(1).getId());
        assertEquals("G", filmDao.getMPAById(1).getName());
    }

    @Test
    @Order(14)
    public void testGetAllMPA() {
        assertEquals(5, filmDao.getAllMPA().size());
    }

    @Test
    @Order(15)
    public void testGetGenresById() {
        assertEquals(1, filmDao.getGenresById(1).getId());
        assertEquals("Комедия", filmDao.getGenresById(1).getName());
    }
}
