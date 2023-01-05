
CREATE table IF NOT EXISTS users
(
    id       int NOT NULL generated by default as identity,
    email    varchar(64),
    login    varchar(64),
    name     varchar(64),
    birthday date,
    PRIMARY KEY (id)
);


create table if not exists friendships
(
    user_Id   INTEGER REFERENCES users (id),
    friend_Id INTEGER REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS films
(
    id          int NOT NULL generated by default as identity,
    name        varchar(64),
    description varchar(64),
    releaseDate date,
    duration    int,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists genres
(
    GENRE_ID   int  generated by default as identity,
    GENRE_NAME varchar(64) ,
    PRIMARY KEY (GENRE_ID)
);


CREATE TABLE if not exists MPA_NAME(
    Name_MPA varchar(64)  ,
    MPA_id  int generated by default as identity,
    PRIMARY KEY (MPA_id)
   );
CREATE TABLE if not exists MPA(
   MPA_id int REFERENCES  MPA_NAME(MPA_id),
   Film_id  int REFERENCES films (id)
);



CREATE TABLE IF NOT EXISTS film_genre
(
    FILM_ID int not null ,
    GENRE_ID int not null ,
    CONSTRAINT fk_film_genre_id
        FOREIGN KEY (FILM_ID)
            REFERENCES films (id),
    CONSTRAINT fk_genre_film_id
        FOREIGN KEY (GENRE_ID)
            REFERENCES genres (GENRE_ID),
    constraint film_genre_pk PRIMARY KEY (FILM_ID, GENRE_ID)

);

CREATE TABLE IF NOT EXISTS likes
(
    film_id int NOT NULL,
    user_id int NOT NULL,
    CONSTRAINT fk_like_film_id
        FOREIGN KEY (film_id)
            REFERENCES films (id),
    CONSTRAINT fk_like_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id),
   constraint likes_pk PRIMARY KEY (film_id, user_id)
);





