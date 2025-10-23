package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.CoursesEntity;
import com.gaodun.learningservice.manager.CoursesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    
    // 修改为GET请求并使用RequestParam接收参数，便于测试
    @GetMapping("/getById")
    public CoursesEntity getById(@RequestParam Integer id) {
        log.info("Received request to get course by id: {}", id);
        return coursesManager.selectById(id);
    }
    
    // 添加一个简单的测试端点
    @GetMapping("/test")
    public String test() {
        return "Service is working!";
    }
}
