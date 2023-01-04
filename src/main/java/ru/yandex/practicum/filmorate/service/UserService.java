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
/*
    public void removeFriend(int userId, int friendId) {
        inMemoryUserStorage.getUsers().get(userId).getFriends().remove(friendId);
        inMemoryUserStorage.getUsers().get(friendId).getFriends().remove(userId);
    }

 */
/*
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

 */
/*
    public List<User> getFriend(int id) {
        List<User> friends = new ArrayList<>();
        for (Integer friend : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            friends.add(inMemoryUserStorage.getUsers().get(friend));
        }
        return friends;
    }


 */


    public User updateUser(User user) {
         return usersDao.updateUser(user);
    }
/*
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

 */

/*
    public User getUser(int id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        return inMemoryUserStorage.getUsers().get(id);
    }

 */


    public Optional<User>getUserById(int  id){
        return usersDao.findUserById(id);
    }

    public Optional<User> addUser(User user){
        return usersDao.addUser(user);
    }
public void  addFriend(int userId,int friendId){
    if (userId <= 0 || friendId <= 0) {
        throw new UserNotFoundException("Id должен быть больше нуля.");
    }
    usersDao.addFriend(userId,friendId);
}
 public Collection<User> getAllUsers(){
     return usersDao.getAllUsers();
 }

 public List<Optional<User>> getFriends(int id){
        return usersDao.getFriends(id);
 }
 public Collection<Optional<User>> getCommonFriend(int id,int otherId){
        return usersDao.getCommonFriend(id,otherId);
 }
public void removeFriend(int userId, int friendId){
        usersDao.removeFriend(userId,friendId);
}
}
