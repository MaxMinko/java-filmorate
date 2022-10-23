package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyFriendsException extends RuntimeException{
    public UserAlreadyFriendsException(String message) {
        super(message);
    }
}
