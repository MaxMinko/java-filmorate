package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int id) {
        return userService.getFriend(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        return userService.getCommonFriend(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.removeFriend(id, friendId);
    }


}
