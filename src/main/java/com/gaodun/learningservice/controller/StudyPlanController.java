package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.DTO.StudyPlanDTO;
import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.Entity.StudyPlanEntity;
import com.gaodun.learningservice.Entity.UserLearningProgressEntity;
import com.gaodun.learningservice.manager.StudyPlanManager;
import com.gaodun.learningservice.manager.UserLearningProgressManager;
import com.gaodun.learningservice.mapper.KnowledgePointsMapper;
import java.util.ArrayList;
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
    
    @Autowired
    private UserLearningProgressManager userLearningProgressManager;
    
    @Autowired
    private KnowledgePointsMapper knowledgePointsMapper;
    
    /**
     * 生成学习计划
     *
     * @return 生成的学习计划列表
     */
    @PostMapping("/generate")
    public List<StudyPlanDTO> generateStudyPlan(@RequestBody Map<String, Integer> requestBody) {
        // 从请求体中获取参数
        Integer userId = requestBody.get("userId");
        Integer courseId = requestBody.get("courseId");
        log.info("接收到生成学习计划请求: userId={}, courseId={}", userId, courseId);
        
        try {
            List<StudyPlanEntity> studyPlans = studyPlanManager.generateStudyPlan(userId, courseId);
            
            // 转换为包含知识点名称的DTO列表
            List<StudyPlanDTO> studyPlanDTOs = new ArrayList<>();
            for (StudyPlanEntity plan : studyPlans) {
                StudyPlanDTO dto = new StudyPlanDTO(plan);
                // 查询知识点名称
                KnowledgePointsEntity knowledgePoint = knowledgePointsMapper.selectById(plan.getKnowledgePoint());
                if (knowledgePoint != null) {
                    dto.setKnowledgePointName(knowledgePoint.getTitle());
                }
                studyPlanDTOs.add(dto);
            }

            
            return studyPlanDTOs;
            
        } catch (Exception e) {
            log.error("生成学习计划失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "生成学习计划失败: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return null;
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
            
            // 转换为包含知识点名称的DTO列表
            List<StudyPlanDTO> studyPlanDTOs = new ArrayList<>();
            for (StudyPlanEntity plan : studyPlans) {
                StudyPlanDTO dto = new StudyPlanDTO(plan);
                // 查询知识点名称
                KnowledgePointsEntity knowledgePoint = knowledgePointsMapper.selectById(plan.getKnowledgePoint());
                if (knowledgePoint != null) {
                    dto.setKnowledgePointName(knowledgePoint.getTitle());
                }
                studyPlanDTOs.add(dto);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", studyPlanDTOs);
            response.put("count", studyPlanDTOs.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("查询学习计划失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "查询学习计划失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 学习计划查询接口
     * @param requestBody 请求体，包含userId和courseId参数
     * @return 学习计划列表（包含知识点名称）
     */
    @PostMapping("/query")
    public List<StudyPlanDTO> queryStudyPlan(
            @RequestBody Map<String, Integer> requestBody) {
        // 从请求体中获取参数
        Integer userId = requestBody.get("userId");
        Integer courseId = requestBody.get("courseId");
        
        log.info("学习计划查询请求: userId={}, courseId={}", userId, courseId);
        
        // 参数校验
        if (userId == null || courseId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "用户ID和课程ID不能为空");
            
            return null;
        }
        
        try {
            List<StudyPlanEntity> studyPlans = studyPlanManager.selectByUserIdAndCourseId(userId, courseId);
            
            // 转换为包含知识点名称的DTO列表
            List<StudyPlanDTO> studyPlanDTOs = new ArrayList<>();
            for (StudyPlanEntity plan : studyPlans) {
                StudyPlanDTO dto = new StudyPlanDTO(plan);
                // 查询知识点名称
                KnowledgePointsEntity knowledgePoint = knowledgePointsMapper.selectById(plan.getKnowledgePoint());
                if (knowledgePoint != null) {
                    dto.setKnowledgePointName(knowledgePoint.getTitle());
                }
                studyPlanDTOs.add(dto);
            }
            
            return studyPlanDTOs;
            
        } catch (Exception e) {
            log.error("学习计划查询失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "学习计划查询失败: " + e.getMessage());
            
            return null;
        }
    }
    
    /**
     * 获取知识图谱图片
     * @param requestBody
     * @return 知识图谱图片数据
     */
    @PostMapping("/knowledge-pic")
    public String getKnowledgePic(
            @RequestBody Map<String, Integer> requestBody) {
        // 从请求体中获取参数
        Integer userId = requestBody.get("userId");
        Integer courseId = requestBody.get("courseId");
        log.info("查询知识图谱图片: userId={}, courseId={}", userId, courseId);
        
        try {
            UserLearningProgressEntity progress = userLearningProgressManager.selectByUserIdAndCourseId(Long.valueOf(userId), Long.valueOf(courseId));
            
            if (progress == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "未找到对应用户的学习进度数据");
                return null;
            }
            
            if (progress.getKnowledgePic() == null || progress.getKnowledgePic().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "知识图谱图片数据不存在");
                return null;
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "知识图谱图片获取成功");
            response.put("data", progress.getKnowledgePic());
            
            return progress.getKnowledgePic();
            
        } catch (Exception e) {
            log.error("获取知识图谱图片失败", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取知识图谱图片失败: " + e.getMessage());
            
            return null;
        }
    }
}
