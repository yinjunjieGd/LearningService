package com.gaodun.learningservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testdb")
@Slf4j
public class TestDbController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/connection")
    public String testConnection() {
        try {
            // 测试数据库连接
            jdbcTemplate.execute("SELECT 1");
            return "Database connection is working!";
        } catch (Exception e) {
            log.error("Database connection test failed", e);
            return "Database connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/course")
    public Object getCourse(@RequestParam Integer id) {
        try {
            log.info("Testing direct database query for course with id: {}", id);
            // 直接执行SQL查询，不通过MyBatis
            String sql = "SELECT course_id FROM courses WHERE course_id = ?";
            
            // 先查看表结构，确认字段名
            String tableInfoSql = "SHOW COLUMNS FROM courses";
            List<Map<String, Object>> tableInfo = jdbcTemplate.queryForList(tableInfoSql);
            log.info("Courses table columns: {}", tableInfo);
            
            // 查看是否存在数据
            String countSql = "SELECT COUNT(*) FROM courses";
            Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
            log.info("Total courses in database: {}", count);
            
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(sql, id);
                log.info("Query result for id {}: {}", id, result);
                return result;
            } catch (Exception e) {
                log.error("Query for id {} failed", id, e);
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("success", false);
                errorMap.put("message", e.getMessage());
                errorMap.put("query", sql);
                errorMap.put("id", id);
                errorMap.put("totalRecords", count);
                return errorMap;
            }
        } catch (Exception e) {
            log.error("Database query failed", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", e.getMessage());
            errorMap.put("errorType", e.getClass().getName());
            return errorMap;
        }
    }
    
    @GetMapping("/list")
    public Object listCourses() {
        try {
            String sql = "SELECT * FROM courses LIMIT 10";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            log.info("Found {} courses", results.size());
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("data", results);
            responseMap.put("count", results.size());
            return responseMap;
        } catch (Exception e) {
            log.error("Failed to list courses", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", e.getMessage());
            return errorMap;
        }
    }
}