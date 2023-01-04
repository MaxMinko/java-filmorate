package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UsersDao {
Optional<User> findUserById(int id);

  Optional<User> addUser(User user);
User updateUser(User user);

void addFriend(int userId,int friendId);

Collection<User> getAllUsers();
 List<Optional<User>> getFriends(int id);
 Collection<Optional<User>> getCommonFriend(int userId, int friendId);
    void removeFriend(int userId, int friendId);
}
