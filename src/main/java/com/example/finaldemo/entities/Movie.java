package com.example.finaldemo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String studio;
    private String director;
    private Integer releaseYear;
    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private List<String> movieCast;

}
