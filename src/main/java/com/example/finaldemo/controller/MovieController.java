package com.example.finaldemo.controller;


import com.example.finaldemo.entities.Movie;
import com.example.finaldemo.services.interfaces.IMovieService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.Serializable;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController  {
    private final IMovieService movieService;

    @QueryMapping
    List<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }

}
