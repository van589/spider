package com.mapper;

import com.pojo.Movie;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class MovieImpl implements MovieMapper {
    private SqlSessionTemplate sqlSession;


    public SqlSessionTemplate getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void insertMovieForeach(List<Movie> list) {
        sqlSession.selectList("com.mapper.MovieMapper.insertMovieForeach", list);
    }
}
