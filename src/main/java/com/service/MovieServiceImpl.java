package com.service;

import com.mapper.MovieMapper;
import com.pojo.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MovieService")
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    public MovieMapper getUserMapper() {
        return movieMapper;
    }

    public void setUserMapper(MovieMapper userMapper) {
        this.movieMapper = userMapper;
    }

    public void insertMovie(List<Movie> list) {
        movieMapper.insertMovieForeach(list);
    }
}
