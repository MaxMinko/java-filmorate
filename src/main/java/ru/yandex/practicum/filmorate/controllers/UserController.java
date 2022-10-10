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
    private Map<String, User> users = new HashMap();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: " + users.size());
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validationUser(user);
        log.info(user.toString());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                throw new ValidationException("Электронная почта не может быть пустой.");
            }
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
        }
        users.put(user.getEmail(), user);
        log.info(user.toString());
        return user;
    }

    public void validationUser(User user) {
        try {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта должна быть непустой и должна содержать @");
            }
            if (user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("День рождение не может быть в будущем.");
            }
            if (users.containsKey(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email существует.");
            }
            users.put(user.getEmail(), user);
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
