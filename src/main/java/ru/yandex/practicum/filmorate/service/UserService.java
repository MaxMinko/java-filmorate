package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UsersDao usersDao;


    @Autowired
    public UserService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }


    public User updateUser(User user) {
        return usersDao.updateUser(user);
    }


    public Optional<User> getUserById(int id) {
        return usersDao.findUserById(id);
    }

    public Optional<User> addUser(User user) {
        return usersDao.addUser(user);
    }

    public void addFriend(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new UserNotFoundException("Id должен быть больше нуля.");
        }
        usersDao.addFriend(userId, friendId);
    }

    public Collection<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    public List<Optional<User>> getFriends(int id) {
        return usersDao.getFriends(id);
    }

    public Collection<Optional<User>> getCommonFriend(int id, int otherId) {
        return usersDao.getCommonFriend(id, otherId);
    }

    public void removeFriend(int userId, int friendId) {
        usersDao.removeFriend(userId, friendId);
    }
}
