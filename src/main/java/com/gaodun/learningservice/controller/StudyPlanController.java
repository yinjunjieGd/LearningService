package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.StudyPlanEntity;
import com.gaodun.learningservice.manager.StudyPlanManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习计划控制器
 * @author shkstart
 */
@RestController
@RequestMapping("/api/study-plan")
@Slf4j
public class StudyPlanController {
    
    @Autowired
    private StudyPlanManager studyPlanManager;
    
    /**
     * 生成学习计划
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 生成的学习计划列表
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateStudyPlan(
            @RequestParam("userId") Integer userId,
            @RequestParam("courseId") Integer courseId) {
        
        log.info("接收到生成学习计划请求: userId={}, courseId={}", userId, courseId);
        
        try {
            List<StudyPlanEntity> studyPlans = studyPlanManager.generateStudyPlan(userId, courseId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "学习计划生成成功");
            response.put("data", studyPlans);
            response.put("count", studyPlans.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("生成学习计划失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "生成学习计划失败: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 查询学习计划
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 学习计划列表
     */
    @GetMapping
    public ResponseEntity<?> getStudyPlan(
            @RequestParam("userId") Integer userId,
            @RequestParam("courseId") Integer courseId) {
        
        log.info("查询学习计划: userId={}, courseId={}", userId, courseId);
        
        try {
            List<StudyPlanEntity> studyPlans = studyPlanManager.selectByUserIdAndCourseId(userId, courseId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", studyPlans);
            response.put("count", studyPlans.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("查询学习计划失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "查询学习计划失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
