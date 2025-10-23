package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.CoursesEntity;
import com.gaodun.learningservice.manager.CoursesManager;
import com.gaodun.learningservice.mapper.CoursesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
