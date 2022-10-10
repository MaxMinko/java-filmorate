package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        try {
            userController.createUser(user);

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(1, userController.getAllUsers().size(), "Неверное количевто пользователей");
    }

    @Test
    public void createUser() {
        try {
            userController.createUser(user);

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(true, userController.getAllUsers().contains(user), "Пользователи не совпадают.");
    }

    @Test
    public void updateUser() {
        User updatedUser = new User(1, "UpdatedTestEmail@", "updatedTestLogin", "UpdatedTestName",
                LocalDate.of(2001, 1, 1));
        try {
            userController.createUser(user);
            userController.updateUser(updatedUser);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(true, userController.getAllUsers().contains(updatedUser), "Пользователь не обновился.");
    }

    @Test
    public void createUserWithIncorrectEmail() {
        user.setEmail("emailAdress");
        try {
            userController.createUser(user);

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }

    @Test
    public void createUserWithEmptyEmail() {
        user.setEmail("");
        try {
            userController.createUser(user);

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }

    @Test
    public void createUserWithEmptyName() {
        user.setName("");
        try {
            userController.createUser(user);

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(0, users.size(), "Имя и логин не совпадают.");
    }

    @Test
    public void createUserWithIncorrectBirthday() {
        user.setBirthday(LocalDate.of(3000, 12, 12));
        try {
            userController.createUser(user);

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }
}
