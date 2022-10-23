package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    User deleteUser(User user);

    User updateUser(User user);

    Map<Integer, User> getUsers();

    Collection<User> getAllUsers();
}
