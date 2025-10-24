package com.gaodun.learningservice.manager.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaodun.learningservice.DTO.VolcEngineResponse;
import com.gaodun.learningservice.DTO.VolcEngineTextRequest;
import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.Entity.StudyPlanEntity;
import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;
import com.gaodun.learningservice.manager.StudyPlanManager;
import com.gaodun.learningservice.manager.VolcEngineService;
import com.gaodun.learningservice.mapper.KnowledgePointsMapper;
import com.gaodun.learningservice.mapper.StudyPlanMapper;
import com.gaodun.learningservice.mapper.UserLearningProfilesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 学习计划Manager实现类
 * @author shkstart
 */
@Service
@Slf4j
public class StudyPlanManagerImpl implements StudyPlanManager {
    
    @Autowired
    private StudyPlanMapper studyPlanMapper;
    
    @Autowired
    private KnowledgePointsMapper knowledgePointsMapper;
    
    @Autowired
    private UserLearningProfilesMapper userLearningProfilesMapper;
    
    @Autowired
    private VolcEngineService volcEngineService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StudyPlanEntity> generateStudyPlan(Integer userId, Integer courseId) {
        log.info("开始为用户 {} 课程 {} 生成学习计划", userId, courseId);
        
        // 1. 查询课程下的所有知识点
        List<KnowledgePointsEntity> knowledgePoints = knowledgePointsMapper.selectByCourseId(courseId);
        if (knowledgePoints == null || knowledgePoints.isEmpty()) {
            log.warn("课程 {} 没有知识点数据", courseId);
            return Collections.emptyList();
        }
        
        // 2. 查询用户学习画像
        UserLearningProfilesEntity profile = userLearningProfilesMapper.selectByUserIdAndCourseId(userId, courseId);
        Map<String, Map<String, Object>> masteryData = new HashMap<>();
        if (profile != null && profile.getMasteryLevel() != null) {
            try {
                masteryData = objectMapper.readValue(
                    profile.getMasteryLevel(),
                    new TypeReference<Map<String, Map<String, Object>>>() {}
                );
            } catch (Exception e) {
                log.error("解析用户学习画像失败", e);
            }
        }
        
        // 3. 构建AI提示词
        String prompt = buildStudyPlanPrompt(knowledgePoints, masteryData);
        log.debug("AI提示词: {}", prompt);
        
        // 4. 调用火山引擎生成学习计划
        List<StudyPlanEntity> studyPlans = callAIToGeneratePlan(userId, courseId, prompt);
        
        // 5. 保存或更新学习计划
        saveStudyPlans(userId, courseId, studyPlans);
        
        log.info("成功为用户 {} 课程 {} 生成 {} 条学习计划", userId, courseId, studyPlans.size());
        return studyPlans;
    }
    
    @Override
    public List<StudyPlanEntity> selectByUserIdAndCourseId(Integer userId, Integer courseId) {
        return studyPlanMapper.selectByUserIdAndCourseId(userId, courseId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdate(StudyPlanEntity studyPlan) {
        StudyPlanEntity existing = studyPlanMapper.selectByUserIdAndCourseIdAndKnowledgePoint(
            studyPlan.getUserId(), 
            studyPlan.getCourseId(), 
            studyPlan.getKnowledgePoint()
        );
        
        if (existing != null) {
            studyPlan.setPlanId(existing.getPlanId());
            return studyPlanMapper.update(studyPlan);
        } else {
            return studyPlanMapper.insert(studyPlan);
        }
    }
    
    /**
     * 构建学习计划生成提示词
     */
    private String buildStudyPlanPrompt(List<KnowledgePointsEntity> knowledgePoints,
                                       Map<String, Map<String, Object>> masteryData) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个智能学习规划助手。请根据以下信息为学生生成个性化学习计划：\n\n");
        
        prompt.append("【课程知识点列表】\n");
        for (KnowledgePointsEntity point : knowledgePoints) {
            prompt.append(String.format("- 知识点ID: %d, 名称: %s, 描述: %s\n",
                point.getPointId(), point.getTitle(), point.getDescription()));
        }
        
        prompt.append("\n【学生掌握度情况】\n");
        if (masteryData.isEmpty()) {
            prompt.append("暂无掌握度数据，学生为新手\n");
        } else {
            for (Map.Entry<String, Map<String, Object>> entry : masteryData.entrySet()) {
                Map<String, Object> mastery = entry.getValue();
                prompt.append(String.format("- 知识点 %s: 掌握度 %.2f, 备注: %s\n",
                    entry.getKey(),
                    mastery.get("score"),
                    mastery.get("comment")));
            }
        }
        
        prompt.append("\n【任务要求】\n");
        prompt.append("请基于上述信息，为每个知识点生成学习计划，要求：\n");
        prompt.append("1. 学习时长(study_duration): 单位为小时，保留2位小数\n");
        prompt.append("2. 学习顺序(learning_order): 从1开始的连续整数\n");
        prompt.append("3. AI分析(ai_comment): 对该知识点的学习建议，考虑权重和掌握度\n\n");
        prompt.append("注意：请根据知识点重要性和难度合理设置学习时长。\n\n");

        prompt.append("【输出格式】\n");
        prompt.append("请严格按照以下JSON格式输出，不要添加任何markdown标记或其他说明文字：\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"knowledge_point\": 知识点ID数字,\n");
        prompt.append("    \"study_duration\": 学习时长长小数,\n");
        prompt.append("    \"learning_order\": 学习顺序数字,\n");
        prompt.append("    \"ai_comment\": \"AI分析建议\"\n");
        prompt.append("  }\n");
        prompt.append("]\n");
        
        return prompt.toString();
    }
    
    /**
     * 调用AI生成学习计划
     */
    private List<StudyPlanEntity> callAIToGeneratePlan(Integer userId, Integer courseId, String prompt) {
        try {
            // 构建火山引擎请求
            VolcEngineTextRequest request = new VolcEngineTextRequest();
            VolcEngineTextRequest.Message message = new VolcEngineTextRequest.Message();
            message.setRole("user");
            message.setContent(prompt);
            request.setMessages(Collections.singletonList(message));
            request.setTemperature(0.7);
            request.setMaxTokens(4000); // 增加token限制以避免响应被截断
            
            // 调用火山引擎
            VolcEngineResponse response = volcEngineService.textInference(request);
            
            // 解析AI返回的JSON
            String aiResponse = response.getChoices().get(0).getMessage().getContent();
            log.info("AI返回原始内容: {}", aiResponse);
            
            // 清理可能的markdown标记和其他格式
            aiResponse = aiResponse.replaceAll("```json\\s*", "")
                                   .replaceAll("```\\s*", "")
                                   .trim();
            
            // 检查JSON是否完整
            if (!aiResponse.endsWith("]") && !aiResponse.endsWith("}")) {
                log.warn("AI返回的JSON可能被截断，尝试修复");
                // 尝试找到最后一个完整的对象
                int lastCompleteObject = aiResponse.lastIndexOf("}");
                if (lastCompleteObject > 0) {
                    aiResponse = aiResponse.substring(0, lastCompleteObject + 1) + "\n]";
                    log.info("修复后的JSON: {}", aiResponse);
                }
            }
            
            // 解析JSON为学习计划列表
            List<Map<String, Object>> planMaps = objectMapper.readValue(
                aiResponse,
                new TypeReference<List<Map<String, Object>>>() {}
            );
            
            // 转换为StudyPlanEntity
            List<StudyPlanEntity> studyPlans = new ArrayList<>();
            for (Map<String, Object> planMap : planMaps) {
                StudyPlanEntity plan = new StudyPlanEntity();
                plan.setUserId(userId);
                plan.setCourseId(courseId);
                
                // 处理knowledge_point字段 - 如果是字符串尝试转换为整数
                Object knowledgePointObj = planMap.get("knowledge_point");
                if (knowledgePointObj instanceof String) {
                    try {
                        plan.setKnowledgePoint(Integer.parseInt((String) knowledgePointObj));
                    } catch (NumberFormatException e) {
                        // 如果无法转换，记录警告并设置默认值
                        log.warn("无法将知识点名称 '{}' 转换为ID，使用默认值0", knowledgePointObj);
                        plan.setKnowledgePoint(0);
                    }
                } else if (knowledgePointObj instanceof Number) {
                    plan.setKnowledgePoint(((Number) knowledgePointObj).intValue());
                }
                
                // 处理study_duration字段 - 支持新格式，同时兼容旧格式
                if (planMap.containsKey("study_duration")) {
                    plan.setStudyDuration(((Number) planMap.get("study_duration")).doubleValue());
                } else if (planMap.containsKey("priority") && planMap.containsKey("estimated_time")) {
                    // 兼容旧格式：根据优先级和预计时间计算学习时长（小时）
                    int priority = ((Number) planMap.get("priority")).intValue();
                    int estimatedTime = ((Number) planMap.get("estimated_time")).intValue();
                    double studyDuration = (estimatedTime * priority / 10.0) / 60.0;
                    plan.setStudyDuration(Math.round(studyDuration * 100.0) / 100.0);
                }
                
                // 可选：如果AI仍然返回difficulty_level字段，则记录日志但不设置
                if (planMap.containsKey("difficulty_level")) {
                    log.debug("AI返回了difficulty_level字段，但已不再使用: {}", planMap.get("difficulty_level"));
                }
                
                plan.setLearningOrder(((Number) planMap.get("learning_order")).intValue());
                plan.setAiComment((String) planMap.get("ai_comment"));
                studyPlans.add(plan);
            }
            
            // 按学习顺序排序
            studyPlans.sort(Comparator.comparing(StudyPlanEntity::getLearningOrder));
            
            return studyPlans;
            
        } catch (Exception e) {
            log.error("调用AI生成学习计划失败", e);
            throw new RuntimeException("生成学习计划失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 保存学习计划（存在则更新，不存在则创建）
     */
    private void saveStudyPlans(Integer userId, Integer courseId, List<StudyPlanEntity> studyPlans) {
        for (StudyPlanEntity plan : studyPlans) {
            StudyPlanEntity existing = studyPlanMapper.selectByUserIdAndCourseIdAndKnowledgePoint(
                userId, courseId, plan.getKnowledgePoint()
            );
            
            if (existing != null) {
                // 更新现有计划
                plan.setPlanId(existing.getPlanId());
                studyPlanMapper.update(plan);
            } else {
                // 创建新计划
                studyPlanMapper.insert(plan);
            }
        }
    }
}
