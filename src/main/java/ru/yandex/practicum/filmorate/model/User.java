package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private Date birthday;
    private final Set<Integer> friends=new HashSet<>();
}
