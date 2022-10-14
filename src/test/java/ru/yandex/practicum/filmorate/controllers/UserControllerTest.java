package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private Map<Integer, User> users;
    UserController userController;
    User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        users = new HashMap<>();
        user = new User(1, "testEmail@", "testLogin", "testName", LocalDate.of(2000, 10,
                11));
    }

    @Test
    public void getAllUsers() {
        userController.createUser(user);
        assertEquals(1, userController.getAllUsers().size(), "Неверное количевто пользователей");
    }

    @Test
    public void createUser() {
        userController.createUser(user);
        assertEquals(true, userController.getAllUsers().contains(user), "Пользователи не совпадают.");
    }

    @Test
    public void updateUser() {
        User updatedUser = new User(1, "UpdatedTestEmail@", "updatedTestLogin", "UpdatedTestName",
                LocalDate.of(2001, 1, 1));
        userController.createUser(user);
        userController.updateUser(updatedUser);
        assertEquals(true, userController.getAllUsers().contains(updatedUser), "Пользователь не обновился.");
    }

    @Test
    public void createUserWithIncorrectEmail() {
        user.setEmail("emailAdress");
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.createUser(user);
            }
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ - @", exception.getMessage());
        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }

    @Test
    public void createUserWithEmptyEmail() {
        user.setEmail("");
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.createUser(user);
            }
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ - @", exception.getMessage());
        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }

    @Test
    public void createUserWithEmptyName() throws ValidationException {
        user.setName("");
        userController.createUser(user);
        assertEquals(0, users.size(), "Имя и логин не совпадают.");
    }

    @Test
    public void createUserWithIncorrectBirthday() {
        user.setBirthday(LocalDate.of(3000, 12, 12));
        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                userController.createUser(user);
            }
        });
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }
}
