package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.CoursesEntity;
import com.gaodun.learningservice.manager.CoursesManager;
import com.gaodun.learningservice.mapper.CoursesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shkstart
 * @create 2025-10-23 0:58
 */
@Slf4j
@Service
public class CoursesImpl implements CoursesManager {
    @Autowired
    private CoursesMapper coursesMapper;
    
    @Override
    public CoursesEntity selectById(Integer id) {
        log.info("selectById: {}", id);
        return coursesMapper.selectById(id);
    }
    
    @Override
    public List<CoursesEntity> selectAll() {
        log.info("selectAll");
        return coursesMapper.selectAll();
    }
    
    @Override
    public List<CoursesEntity> selectByTeacherId(Integer teacherId) {
        log.info("selectByTeacherId: {}", teacherId);
        return coursesMapper.selectByTeacherId(teacherId);
    }
    
    @Override
    public int insert(CoursesEntity course) {
        log.info("insert: {}", course);
        return coursesMapper.insert(course);
    }
    
    @Override
    public int update(CoursesEntity course) {
        log.info("update: {}", course);
        return coursesMapper.update(course);
    }
    
    @Override
    public int delete(Integer id) {
        log.info("delete: {}", id);
        return coursesMapper.delete(id);
    }
}
