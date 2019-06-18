package com.mapper;

import com.pojo.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MovieMapper {
    void insertMovieForeach(List<Movie> list);
}