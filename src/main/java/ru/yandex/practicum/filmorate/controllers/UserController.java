package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @GetMapping()
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: " + users.size());
        return users.values();
    }


    @PostMapping()
    public User createUser(@RequestBody User user) {
        checkId(user);
        checkEmail(user);
        checkLogin(user);
        checkBirthday(user);
        if (user.getName() == null) {
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

    @PutMapping()
    public User updateUser(@RequestBody User user)  {
        if (!users.containsKey(user.getId())) {
            log.error("Такого пользователя не существует!");
            throw new ValidationException("Такого пользователя не существует!");
        } else {
            if (user.getName().isBlank() || user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь обновлен");
            return user;
        }
    }

    private void checkLogin(User user) {
        if (user.getLogin().contains("") && user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы!");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
    }

    private void checkEmail(User user)  {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ - @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
        }
    }

    private void checkBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

    private void checkId(User user)  {
        if (users.containsKey(user.getId())) {
            log.error("Такой пользователь уже существует!");
            throw new ValidationException("Такой пользователь уже существует!");
        }
    }
}
