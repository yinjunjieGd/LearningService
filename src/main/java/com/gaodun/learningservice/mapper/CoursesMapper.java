package com.gaodun.learningservice.mapper;


import com.gaodun.learningservice.Entity.CoursesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shkstart
 * @create 2025-10-22 23:57
 */
@Mapper
public interface CoursesMapper {
    // 根据ID查询课程
    CoursesEntity selectById(Integer id);
    
    // 查询所有课程
    List<CoursesEntity> selectAll();
    
    // 根据教师ID查询课程
    List<CoursesEntity> selectByTeacherId(Integer teacherId);
    
    // 插入课程
    int insert(CoursesEntity course);
    
    // 更新课程
    int update(CoursesEntity course);
    
    // 删除课程
    int delete(Integer id);
}
