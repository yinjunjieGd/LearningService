package com.gaodun.learningservice.mapper;


import com.gaodun.learningservice.Entity.CoursesEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shkstart
 * @create 2025-10-22 23:57
 */
@Mapper
public interface CoursesMapper {
    CoursesEntity selectById(Integer id);
}
