package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User addUser(User user) {
        if (users.containsValue(user)) {
            throw new ValidationException("Такой пользователь уже есть!");
        }
        checkUser(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            user.setId(userId++);
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь");
            return user;
        } else {
            user.setId(userId++);
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь");
            return user;
        }
    }

    @Override
    public User deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Такого пользователя не существует!");
            throw new UserNotFoundException("Такого пользователя не существует!");
        } else {
            users.remove(user.getId());
            log.info("Пользователь удален.");
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Такого пользователя не существует!");
            throw new UserNotFoundException("Такого пользователя не существует!");
        } else {
            if (user.getName().isBlank() || user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь обновлен");
            return user;
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: " + users.size());
        return users.values();
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    private void checkUser(User user) {
        if (user.getLogin().contains("") && user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы!");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ - @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        if (users.containsKey(user.getId())) {
            log.error("Такой пользователь уже существует!");
            throw new ValidationException("Такой пользователь уже существует!");
        }
    }
}
