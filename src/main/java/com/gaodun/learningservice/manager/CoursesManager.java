package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.CoursesEntity;

/**
 * @author shkstart
 * @create 2025-10-23 0:58
 */
public interface CoursesManager {
    CoursesEntity selectById(Integer id);
}
