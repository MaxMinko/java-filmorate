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

    @GetMapping()
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: " + users.size());
        return users.values();
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        try {
            if (users.containsKey(user.getId())) {
                log.error("Такой пользователь уже существует!, {}", user);
                throw new ValidationException("Такой пользователь уже существует!");
            } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.error("Электронная почта не может быть пустой и должна содержать символ - @, {}", user);
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
            } else if (user.getLogin().isBlank() || user.getLogin().contains(" ") || user.getLogin() == null) {
                log.error("Логин не может быть пустым и содержать пробелы!, {}", user);
                throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть в будущем!, {}", user);
                throw new ValidationException("Дата рождения не может быть в будущем!");
            } else if (user.getName().isBlank() || user.getName() == null) {
                user.setName(user.getLogin());
                users.put(user.getId(), user);
                log.info("Добавлен новый пользователь, {}", user);
                return user;
            } else {
                users.put(user.getId(), user);
                log.info("Добавлен новый пользователь, {}", user);
                return user;
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return user;
        }
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        try {
            if (!users.containsKey(user.getId())) {
                log.error("Такого пользователя не существует!, {}", user);
                throw new ValidationException("Такого пользователя не существует!");
            } else {

                if (user.getName().isBlank() || user.getName() == null) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.info("Пользователь обновлен - , {}", user);
                return user;
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return user;
        }
    }

}
