package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UsersDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UsersDao usersDao;
    private final UserStorage inMemoryUserStorage;

    @Autowired
   public UserService(UserStorage inMemoryUserStorage, UsersDao usersDao) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.usersDao=usersDao;
    }
/*
    public void addFriend(int userId, int friendsId) {
        if (userId <= 0 || friendsId <= 0) {
            throw new UserNotFoundException("Id должен быть больше нуля.");
        }
        inMemoryUserStorage.getUsers().get(userId).getFriends().add(friendsId);
        inMemoryUserStorage.getUsers().get(friendsId).getFriends().add(userId);
    }

 */

    public void removeFriend(int userId, int friendId) {
        inMemoryUserStorage.getUsers().get(userId).getFriends().remove(friendId);
        inMemoryUserStorage.getUsers().get(friendId).getFriends().remove(userId);
    }

    public Collection<User> getCommonFriend(int userId, int friendId) {
        List<User> commonFriend = new ArrayList<>();
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        Set<Integer> userFriend = inMemoryUserStorage.getUsers().get(userId).getFriends();
        Set<Integer> friendFriends = inMemoryUserStorage.getUsers().get(friendId).getFriends();
        for (Integer friend : friendFriends) {
            if (userFriend.contains(friend)) {
                commonFriend.add(inMemoryUserStorage.getUsers().get(friend));
            }
        }
        return commonFriend;
    }

    public List<User> getFriend(int id) {
        List<User> friends = new ArrayList<>();
        for (Integer friend : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            friends.add(inMemoryUserStorage.getUsers().get(friend));
        }
        return friends;
    }



    public void updateUser(User user) {
         usersDao.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }


    public User getUser(int id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        return inMemoryUserStorage.getUsers().get(id);
    }


    public Optional<User>getUserById(int  id){
        return usersDao.findUserById(id);
    }

    public void addUser(User user){
         usersDao.addUser(user);
    }
public void  addFriend(int userId,int friendId){
    usersDao.addFriend(userId,friendId);
}
}
