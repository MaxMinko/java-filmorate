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
        userController.createUser(user);
        assertEquals(1, userController.getAllUsers().size(), "Неверное количевто пользователей");
    }

    @Test
    public void createUser() {
        userController.createUser(user);
        assertEquals(user, userController.getAllUsers().get(1), "Пользователи не совпадают.");
    }

    @Test
    public void updateUser() {
        User updatedUser = new User(1, "UpdatedTestEmail@", "updatedTestLogin", "UpdatedTestName",
                LocalDate.of(2001, 1, 1));
        userController.createUser(user);
        userController.updateUser(updatedUser);
        assertEquals(updatedUser, userController.getAllUsers().get(1), "Пользователь не обновился.");
    }

    @Test
    public void createUserWithIncorrectEmail() {
        user.setEmail("emailAdress");
        userController.createUser(user);
        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }

    @Test
    public void createUserWithEmptyEmail() {
        user.setEmail("");
        userController.createUser(user);
        assertEquals(0, userController.getAllUsers().size(), "Неверное количество пользователей.");
    }
    @Test
    public void createUserWithEmptyName(){
        user.setName("");
        userController.createUser(user);
        assertEquals(user.getLogin(),userController.getAllUsers().get(1).getName(),"Имя и логин не совпадают.");
    }
    @Test
    public void createUserWithIncorrectBirthday(){
       user.setBirthday(LocalDate.of(3000,12,12));
       userController.createUser(user);
       assertEquals(0,userController.getAllUsers().size(),"Неверное количество пользователей.");
    }
}
