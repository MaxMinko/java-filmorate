package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
