package com.example.finaldemo.services.impl;

import com.example.finaldemo.entities.Movie;
import com.example.finaldemo.repository.MovieRepository;
import com.example.finaldemo.services.interfaces.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IMovieServiceImpl implements IMovieService {
    private final MovieRepository movieRepository;
    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
