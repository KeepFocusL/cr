package com.example.cr.user.mapper;

import com.example.cr.user.entity.Course;
import com.example.cr.user.entity.CourseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseMapper {
    long countByExample(CourseExample example);

    int deleteByExample(CourseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Course row);

    int insertSelective(Course row);

    List<Course> selectByExample(CourseExample example);

    Course selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Course row, @Param("example") CourseExample example);

    int updateByExample(@Param("row") Course row, @Param("example") CourseExample example);

    int updateByPrimaryKeySelective(Course row);

    int updateByPrimaryKey(Course row);
}