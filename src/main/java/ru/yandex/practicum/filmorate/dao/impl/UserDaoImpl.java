package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.exception.UserAlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Validator;

import java.util.*;

@Component
public class UserDaoImpl implements UsersDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final Validator validator = new Validator(this);

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
            log.info("Найден пользователь: " + user.getId());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором " + id + " не найден.");
            throw new UserNotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Optional<User> addUser(User user) {
        validator.checkUserForDataBase(user, jdbcTemplate);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            String sqlQuery = "insert into users (email, login,name,birthday)" + "values(?,?,?,?)";
            jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
            log.info("Добавлен новый пользователь");
        } else {
            String sqlQuery = "insert into users (email, login,name,birthday)" + "values(?,?,?,?)";
            jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
            log.info("Добавлен новый пользователь");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users ORDER BY id DESC LIMIT 1");
        User userNew = null;
        if (userRows.next()) {
            userNew = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
        }
        return Optional.of(userNew);
    }

    @Override
    public User updateUser(User user) {
        findUserById(user.getId());
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "update  users set" + " email=?, login=?,name=?,birthday=?" + "where id=?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь обновлен");
        return user;
    }
    @Override
    public void addFriend(int userId, int friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select friend_id from friendships where user_Id=?", userId);
        Set<Integer> userFriend = new HashSet<>();
        while (userRows.next()) {
            userFriend.add(userRows.getInt("friend_id"));
        }
        if (!userFriend.contains(friendId)) {
            String sqlQuery = "insert into friendships (user_Id,friend_Id)" + "values(?,?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
            log.info("Пользователь {} добавлен в друзья " + friendId);
        } else {
            log.info("Пользователь "+userId+" уже ялвяется другом пользователя "+ friendId);
            throw new UserAlreadyFriendsException("Пользователь "+userId+" уже ялвяется другом пользователя " + friendId);
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users");
        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
            SqlRowSet friendShip = jdbcTemplate.queryForRowSet("select friend_id from friendships where user_id=?", userRows.getInt("id"));
            while (friendShip.next()) {
                user.getFriends().add(friendShip.getInt("friend_id"));
            }
            log.info("Найден пользователь: "+ user.getId());
            users.add(user);
        }
        return users;
    }

    @Override
    public List<Optional<User>> getFriends(int id) {
        List<Optional<User>> friends = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select friend_id from friendships  where  user_id = ?", id);
        while (userRows.next()) {
            friends.add(findUserById(userRows.getInt("FRIEND_ID")));
        }
        return friends;
    }

    @Override
    public Collection<Optional<User>> getCommonFriend(int userId, int friendId) {
        List<Optional<User>> commonFriend = new ArrayList<>();
        List<Optional<User>> userFriends = getFriends(userId);
        List<Optional<User>> friendFriends = getFriends(friendId);
        for (Optional<User> friend : friendFriends) {
            if (userFriends.contains(friend)) {
                commonFriend.add(friend);
            }
        }
        return commonFriend;
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        SqlRowSet userRows1 = jdbcTemplate.queryForRowSet("select * from friendships where user_id=?", userId);
        if (userRows1.wasNull()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден.");
        }
        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("select * from friendships where friend_id=?", friendId);
        if (userRows2.wasNull()) {
            throw new UserNotFoundException("Пользователь " + friendId + " не найден.");
        }
        String sqlQuery = "delete from friendships where FRIEND_ID = ? and USER_ID=?";
        jdbcTemplate.update(sqlQuery, friendId, userId);
    }

}

