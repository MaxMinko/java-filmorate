package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Film implements Comparable{
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> likes=new HashSet<>();

    @Override
    public int compareTo(Object o){
        Film comparedFilm =(Film) o;
        return this.getLikes().size()-comparedFilm.getLikes().size();
    }
}
