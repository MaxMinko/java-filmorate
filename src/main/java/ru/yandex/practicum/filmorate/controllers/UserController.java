package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private List< User> users = new ArrayList<>();

    @GetMapping()
    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: " + users.size());
        return users;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) throws ValidationException {
        if (users.contains(user)){
            log.error("Такой пользователь уже существует!, {}", user);
            throw new ValidationException("Такой пользователь уже существует!");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")){
            log.error("Электронная почта не может быть пустой и должна содержать символ - @, {}", user);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")){
            log.error("Логин не может быть пустым и содержать пробелы!, {}", user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        } else if (user.getBirthday().isAfter(LocalDate.now())){
            log.error("Дата рождения не может быть в будущем!, {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем!");
        } else if (user.getName().isBlank()||user.getName()==null){
            user.setName(user.getLogin());
            users.add(user);
            log.info("Добавлен новый пользователь, {}", user);
            return user;
        } else {
            users.add(user);
            log.info("Добавлен новый пользователь, {}", user);
            return user;
        }
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (!users.contains(user)){
            log.error("Такого пользователя не существует!, {}", user);
            throw new ValidationException("Такого пользователя не существует!");
        } else {
            users.remove(user);
            if (user.getName().isBlank()||user.getName()==null){
                user.setName(user.getLogin());
            }
            users.add(user);
            log.info("Пользователь обновлен - , {}", user);
            return user;
        }

    }

}
