package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UsersDao {
Optional<User> findUserById(int id);

  void addUser(User user);
void updateUser(User user);

void addFriend(int userId,int friendId);

Collection<User> getAllUsers();

}
