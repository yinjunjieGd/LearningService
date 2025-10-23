package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.CoursesEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器 - 不涉及数据库操作，只返回固定数据
 * 用于测试API基本功能是否正常
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    // 返回固定的课程数据，不涉及数据库操作
    @GetMapping("/course")
    public CoursesEntity getTestCourse() {
        CoursesEntity course = new CoursesEntity();
        course.setId(1);
        return course;
    }
    
    // 另一个简单的测试端点
    @GetMapping("/hello")
    public String hello() {
        return "Hello from TestController!";
    }
}