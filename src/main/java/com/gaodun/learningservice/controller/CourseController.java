package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.CoursesEntity;
import com.gaodun.learningservice.manager.CoursesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2025-10-22 23:44
 */
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Slf4j
public class CourseController {
    @Autowired
    private CoursesManager coursesManager;
    
    // 根据ID查询课程
    @GetMapping("/getById")
    public CoursesEntity getById(@RequestParam Integer id) {
        log.info("Received request to get course by id: {}", id);
        return coursesManager.selectById(id);
    }
    
    // 查询所有课程
    @GetMapping("/list")
    public List<CoursesEntity> list() {
        log.info("Received request to list all courses");
        return coursesManager.selectAll();
    }
    
    // 根据教师ID查询课程
    @GetMapping("/listByTeacherId")
    public List<CoursesEntity> listByTeacherId(@RequestParam Integer teacherId) {
        log.info("Received request to list courses by teacher id: {}", teacherId);
        return coursesManager.selectByTeacherId(teacherId);
    }
    
    // 插入课程
    @PostMapping("/insert")
    public int insert(@RequestBody CoursesEntity course) {
        log.info("Received request to insert course: {}", course);
        return coursesManager.insert(course);
    }
    
    // 更新课程
    @PutMapping("/update")
    public int update(@RequestBody CoursesEntity course) {
        log.info("Received request to update course: {}", course);
        return coursesManager.update(course);
    }
    
    // 删除课程
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer id) {
        log.info("Received request to delete course by id: {}", id);
        return coursesManager.delete(id);
    }
    
    // 添加一个简单的测试端点
    @GetMapping("/test")
    public String test() {
        return "Service is working!";
    }
    
    // 获取默认3个课程（仅返回course_id和title字段）
    @GetMapping("/getDefaultCourses")
    public List<CoursesEntity> getDefaultCourses() {
        log.info("Received request to get default 3 courses");
        List<CoursesEntity> allCourses = coursesManager.selectAll();
        // 如果课程数量超过3个，返回前3个
        if (allCourses.size() > 3) {
            return allCourses.subList(0, 3);
        }
        return allCourses;
    }
}
