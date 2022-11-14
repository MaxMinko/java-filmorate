package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
@Component
public class UserDaoImpl implements UsersDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;


   public UserDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Optional<User> findUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                     userRows.getString("name")  ,
                    userRows.getDate("birthday"));
            log.info("Найден пользователь: {}", user.getId());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
    @Override
    public void addUser(User user){
       String sqlQuery="insert into users (email, login,name,birthday)"+"values(?,?,?,?)";
       jdbcTemplate.update(sqlQuery,user.getEmail(),user.getLogin(),user.getName(),user.getBirthday());
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery="update  users set"+" email=?, login=?,name=?,birthday=?"+"where id=?";
        jdbcTemplate.update(sqlQuery,user.getEmail(),user.getLogin(),user.getName(),user.getBirthday(),user.getId());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery="insert into friendships (user_Id,friend_Id)"+"values(?,?)";
        jdbcTemplate.update(sqlQuery,userId,friendId);
    }
@Override
   public Collection<User> getAllUsers(){


       return
}
}

