package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class MPA {
    private int id;
    private String name;
    public MPA(int id){
        this.id=id;
    }
}
