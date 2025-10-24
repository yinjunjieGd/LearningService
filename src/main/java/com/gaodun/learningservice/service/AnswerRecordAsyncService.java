package com.gaodun.learningservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaodun.learningservice.DTO.VolcEngineResponse;
import com.gaodun.learningservice.DTO.VolcEngineTextRequest;
import com.gaodun.learningservice.Entity.AnswerRecordsEntity;
import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.Entity.UserLearningRecordsEntity;
import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;
import com.gaodun.learningservice.config.VolcEngineConfig;
import com.gaodun.learningservice.manager.AnswerRecordsManager;
import com.gaodun.learningservice.manager.VolcEngineService;
import com.gaodun.learningservice.mapper.KnowledgePointsMapper;
import com.gaodun.learningservice.mapper.QuestionsMapper;
import com.gaodun.learningservice.mapper.UserLearningRecordsMapper;
import com.gaodun.learningservice.mapper.UserLearningProfilesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 答题记录异步处理服务
 * @author shkstart
 */
@Slf4j
@Service
public class AnswerRecordAsyncService {
    
    @Autowired
    private AnswerRecordsManager answerRecordsManager;
    
    @Autowired
    private UserLearningRecordsMapper userLearningRecordsMapper;
    
    @Autowired
    private KnowledgePointsMapper knowledgePointsMapper;
    
    @Autowired
    private QuestionsMapper questionsMapper;
    
    @Autowired
    private VolcEngineService volcEngineService;
    
    @Autowired
    private VolcEngineConfig volcEngineConfig;
    
    @Autowired
    private UserLearningProfilesMapper userLearningProfilesMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 异步处理答题记录更新后的任务
     * 查询该答题记录关联的用户ID、课程ID以及课程关联的知识点
     * 查找该学员每个知识点前10条的user_learning_records答题数据
     * 
     * @param recordId 答题记录ID
     */
    @Async
    public void processAnswerRecordUpdate(Integer recordId) {
        try {
            System.out.println("[DEBUG] 开始异步处理答题记录更新任务，答题记录ID: " + recordId);
            log.info("开始异步处理答题记录更新任务，答题记录ID: {}", recordId);
            
            // 根据recordId查询答题记录
            AnswerRecordsEntity answerRecord = answerRecordsManager.selectById(recordId);
            System.out.println("[DEBUG] 答题记录查询完成 - recordId: " + recordId + ", 存在: " + (answerRecord != null));
            
            if (answerRecord == null) {
                log.warn("未找到答题记录，recordId: {}", recordId);
                return;
            }
            
            Integer userId = answerRecord.getUserId();
            System.out.println("[DEBUG] 从答题记录中获取用户ID - userId: " + userId);
            
            // 查询该用户的所有学习记录
            List<UserLearningRecordsEntity> learningRecords = 
                userLearningRecordsMapper.selectByUserId(userId);
            
            System.out.println("[DEBUG] 学习记录查询完成 - userId: " + userId + ", 记录数: " + (learningRecords != null ? learningRecords.size() : 0));
            
            if (learningRecords == null || learningRecords.isEmpty()) {
                log.warn("用户没有学习记录，无法生成学习画像 - 用户ID: {}", userId);
                return;
            }
            
            // 获取所有不重复的questionId
            Set<Integer> questionIds = learningRecords.stream()
                .map(UserLearningRecordsEntity::getQuestionId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            
            // 提取所有课程ID
            Set<Integer> courseIds = new HashSet<>();
            
            for (Integer questionId : questionIds) {
                Integer pointId = getPointIdByQuestionId(questionId);
                if (pointId != null) {
                    // 通过知识点ID获取课程ID
                    KnowledgePointsEntity point = knowledgePointsMapper.selectById(pointId);
                    if (point != null && point.getCourseId() != null) {
                        courseIds.add(point.getCourseId());
                    }
                }
            }
            
            System.out.println("[DEBUG] 课程ID处理完成 - courseIds: " + courseIds);
            
            // 按课程ID遍历处理
            for (Integer courseId : courseIds) {
                log.info("处理课程 - courseId: {}", courseId);
                
                // 查询该课程下的所有知识点
                List<KnowledgePointsEntity> allPoints = knowledgePointsMapper.selectByCourseId(courseId);
                if (allPoints == null || allPoints.isEmpty()) {
                    log.warn("课程下没有知识点 - courseId: {}", courseId);
                    continue;
                }
                
                List<KnowledgePointWithRecords> pointsWithRecords = new ArrayList<>();
                Map<String, Map<String, Object>> masteryData = new LinkedHashMap<>();
                
                // 遍历该课程的每个知识点，分类处理
                for (KnowledgePointsEntity point : allPoints) {
                    Integer pointId = point.getPointId();
                    
                    // 查询该知识点的所有学习记录
                    List<UserLearningRecordsEntity> pointRecords = learningRecords.stream()
                        .filter(record -> {
                            Integer qId = record.getQuestionId();
                            if (qId == null) return false;
                            Integer pId = getPointIdByQuestionId(qId);
                            return pointId.equals(pId);
                        })
                        .collect(Collectors.toList());
                    
                    if (pointRecords.isEmpty()) {
                        // 无答题记录：掌握度直接设为0
                        Map<String, Object> result = new HashMap<>();
                        result.put("score", 0.0);
                        result.put("comment", "暂未学习");
                        masteryData.put(String.valueOf(pointId), result);
                        
                        System.out.println("[DEBUG] 知识点无记录，掌握度设为0 - pointId: " + pointId + ", title: " + point.getTitle());
                    } else {
                        // 有答题记录：加入批处理列表
                        pointsWithRecords.add(new KnowledgePointWithRecords(point, pointRecords));
                        System.out.println("[DEBUG] 知识点有记录 - pointId: " + pointId + ", title: " + point.getTitle() + ", 记录数: " + pointRecords.size());
                    }
                }
                
                // 分批处理有记录的知识点
                 if (!pointsWithRecords.isEmpty()) {
                     // 分批处理，每批最多100条答题记录
                     List<List<KnowledgePointWithRecords>> batches = createBatches(pointsWithRecords, 100);
                     
                     System.out.println("[DEBUG] 开始分批AI分析 - courseId: " + courseId + ", 总知识点数: " + pointsWithRecords.size() + ", 批次数: " + batches.size());
                     
                     // 设置超时时间为2分钟
                     long timeoutMs = 2 * 60 * 1000;
                     long startTime = System.currentTimeMillis();
                     
                     for (int batchIndex = 0; batchIndex < batches.size(); batchIndex++) {
                         // 检查是否超时
                         if (System.currentTimeMillis() - startTime > timeoutMs) {
                             log.warn("AI分析超时，已处理 {}/{} 个批次", batchIndex, batches.size());
                             System.out.println("[DEBUG] AI分析超时 - 已处理批次: " + batchIndex + "/" + batches.size());
                             break;
                         }
                         
                         List<KnowledgePointWithRecords> batch = batches.get(batchIndex);
                         int totalRecordsInBatch = batch.stream().mapToInt(KnowledgePointWithRecords::getRecordCount).sum();
                         
                         System.out.println("[DEBUG] 处理批次 " + (batchIndex + 1) + "/" + batches.size() + 
                             " - 知识点数: " + batch.size() + ", 答题记录总数: " + totalRecordsInBatch);
                         
                         // 批量调用AI分析
                         Map<String, Map<String, Object>> batchResults = analyzeBatchKnowledgeMasteryWithAI(userId, batch);
                         
                         // 合并结果
                         if (batchResults != null && !batchResults.isEmpty()) {
                             masteryData.putAll(batchResults);
                             System.out.println("[DEBUG] 批次分析完成 - 获得 " + batchResults.size() + " 个知识点的掌握度结果");
                         } else {
                             System.out.println("[DEBUG] 批次分析未返回结果");
                         }
                     }
                 }
                 
                 // 保存学习画像
                 if (!masteryData.isEmpty()) {
                     System.out.println("[DEBUG] 准备保存学习画像 - userId: " + userId + ", courseId: " + courseId + ", 知识点数: " + masteryData.size());
                     saveUserLearningProfile(userId, courseId, masteryData);
                 } else {
                     log.warn("没有生成任何掌握度数据 - userId: {}, courseId: {}", userId, courseId);
                     System.out.println("[DEBUG] 学习画像为空，未保存 - userId: " + userId + ", courseId: " + courseId);
                 }
             }
             
             log.info("异步处理完成 - recordId: {}", recordId);
            
            System.out.println("[DEBUG] 异步处理答题记录更新任务完成，答题记录ID: " + recordId);
            log.info("异步处理答题记录更新任务完成，答题记录ID: {}", recordId);
            
        } catch (Exception e) {
            System.out.println("[DEBUG] 异步处理答题记录更新任务失败，答题记录ID: " + recordId);
            System.out.println("[DEBUG] 异常信息: " + e.getMessage());
            e.printStackTrace();
            log.error("异步处理答题记录更新任务失败，答题记录ID: {}", recordId, e);
        }
    }
    
    /**
     * 通过问题ID查询知识点ID
     */
    private Integer getPointIdByQuestionId(Integer questionId) {
        try {
            return questionsMapper.selectPointIdByQuestionId(questionId);
        } catch (Exception e) {
            log.error("查询问题 {} 对应的知识点ID失败", questionId, e);
            return null;
        }
    }
    
    /**
     * 使用火山引擎AI分析用户知识点掌握度
     * @return 包含score和comment的Map对象，如果分析失败则返回null
     */
    private Map<String, Object> analyzeKnowledgeMasteryWithAI(Integer userId, Integer pointId, String pointTitle, 
                                                               List<UserLearningRecordsEntity> records) {
        try {
            // 1. 准备分析数据
            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append("学员ID: ").append(userId).append("\n");
            dataBuilder.append("知识点: ").append(pointTitle).append(" (ID: ").append(pointId).append(")\n\n");
            dataBuilder.append("答题记录详情:\n");
            
            // 2. 遍历每条答题记录，提取关键信息
            for (int i = 0; i < records.size(); i++) {
                UserLearningRecordsEntity record = records.get(i);
                dataBuilder.append("第").append(i + 1).append("题:\n");
                dataBuilder.append("  - 是否正确: ").append(record.getIsCorrect() ? "正确" : "错误").append("\n");
                dataBuilder.append("  - 答题时间: ").append(record.getTimeSpent() != null ? record.getTimeSpent() : 0).append("秒\n");
                
                if (record.getAnsweredAt() != null) {
                    dataBuilder.append("  - 答题时刻: ").append(record.getAnsweredAt()).append("\n");
                }
                dataBuilder.append("\n");
            }
            
            // 3. 计算基础统计数据
            long correctCount = records.stream()
                .filter(record -> record.getIsCorrect() != null && record.getIsCorrect())
                .count();
            double accuracy = (double) correctCount / records.size() * 100;
            
            int totalTimeSpent = records.stream()
                .mapToInt(record -> record.getTimeSpent() != null ? record.getTimeSpent() : 0)
                .sum();
            double avgTimeSpent = (double) totalTimeSpent / records.size();
            
            dataBuilder.append("统计数据:\n");
            dataBuilder.append("  - 答题总数: ").append(records.size()).append("\n");
            dataBuilder.append("  - 正确数: ").append(correctCount).append("\n");
            dataBuilder.append("  - 正确率: ").append(String.format("%.2f", accuracy)).append("%\n");
            dataBuilder.append("  - 总答题时长: ").append(totalTimeSpent).append("秒\n");
            dataBuilder.append("  - 平均答题时长: ").append(String.format("%.2f", avgTimeSpent)).append("秒\n");
            
            // 4. 构建火山引擎请求
            VolcEngineTextRequest request = new VolcEngineTextRequest();
            
            List<VolcEngineTextRequest.Message> messages = new ArrayList<>();
            
            // 系统提示词 - 要求返回JSON格式
            String systemPrompt = "你是一位专业的学习分析专家，请根据学员的答题数据，分析该学员对该知识点的掌握程度。" +
                "请给出一个0到1之间的掌握度分数（保留两位小数），其中：" +
                "\n- 0.9-1.0: 完全掌握" +
                "\n- 0.7-0.9: 良好掌握" +
                "\n- 0.5-0.7: 基本掌握" +
                "\n- 0.3-0.5: 部分掌握" +
                "\n- 0.0-0.3: 未掌握" +
                "\n\n评分时请综合考虑：" +
                "\n1. 答题正确率（权重50%）" +
                "\n2. 答题时间（权重30%）- 时间过长或过短都可能说明掌握不牢固" +
                "\n3. 答题趋势（权重20%）- 近期答题表现是否有改善" +
                "\n\n请返回JSON格式的结果，包含两个字段：" +
                "\n{\"score\": 掌握度分数(0-1之间的小数), \"comment\": \"简短的评语(不超过50字)\"}" +
                "\n注意：只返回JSON对象，不要有任何其他文字。";
            
            messages.add(new VolcEngineTextRequest.Message("system", systemPrompt));
            messages.add(new VolcEngineTextRequest.Message("user", dataBuilder.toString()));
            
            request.setModel(volcEngineConfig.getTextModelId());
            request.setMessages(messages);
            request.setTemperature(0.3);
            request.setMaxTokens(100);
            request.setTopP(0.9);
            
            log.info("准备调用火山引擎API分析知识点掌握度，用户ID: {}, 知识点ID: {}", userId, pointId);
            
            // 5. 调用火山引擎API
            VolcEngineResponse response = volcEngineService.textInference(request);
            
            // 6. 解析响应
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String aiResult = response.getChoices().get(0).getMessage().getContent().trim();
                log.info("火山引擎AI分析结果 - 用户: {}, 知识点: {} ({}), 原始结果: {}", 
                    userId, pointId, pointTitle, aiResult);
                
                // 尝试解析为JSON对象
                try {
                    Map<String, Object> resultMap = objectMapper.readValue(aiResult, Map.class);
                    
                    // 验证必需字段
                    if (resultMap.containsKey("score") && resultMap.containsKey("comment")) {
                        Object scoreObj = resultMap.get("score");
                        double masteryScore = scoreObj instanceof Number ? 
                            ((Number) scoreObj).doubleValue() : Double.parseDouble(scoreObj.toString());
                        
                        if (masteryScore >= 0 && masteryScore <= 1) {
                            String comment = resultMap.get("comment").toString();
                            log.info("知识点掌握度分析完成 - 用户: {}, 知识点: {}, 掌握度: {}, 评语: {}", 
                                userId, pointTitle, masteryScore, comment);
                            
                            // 返回结果
                            Map<String, Object> result = new HashMap<>();
                            result.put("score", masteryScore);
                            result.put("comment", comment);
                            return result;
                        } else {
                            log.warn("AI返回的掌握度分数超出范围: {}", masteryScore);
                        }
                    } else {
                        log.warn("AI返回的JSON缺少必需字段: {}", aiResult);
                    }
                } catch (Exception e) {
                    log.warn("无法解析AI返回的JSON结果: {}", aiResult, e);
                }
            } else {
                log.warn("火山引擎API返回为空或无有效内容");
            }
            
        } catch (Exception e) {
            log.error("使用火山引擎分析知识点掌握度失败，用户ID: {}, 知识点ID: {}", userId, pointId, e);
        }
        
        return null;
    }
    
    /**
     * 保存或更新用户学习画像
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param masteryData 掌握度数据，格式: {知识点id: {score: 值, comment: 评语}}
     */
    private void saveUserLearningProfile(Integer userId, Integer courseId, 
                                         Map<String, Map<String, Object>> masteryData) {
        try {
            // 将掌握度数据转换为JSON字符串
            String masteryLevelJson = objectMapper.writeValueAsString(masteryData);
            
            // 根据用户ID和课程ID查询是否已存在该学习画像
            UserLearningProfilesEntity existingProfile = userLearningProfilesMapper.selectByUserIdAndCourseId(userId, courseId);
            
            if (existingProfile != null) {
                // 更新现有记录的masteryLevel和lastUpdated字段
                existingProfile.setMasteryLevel(masteryLevelJson);
                existingProfile.setLastUpdated(new java.sql.Timestamp(System.currentTimeMillis()));
                
                int updated = userLearningProfilesMapper.update(existingProfile);
                if (updated > 0) {
                    log.info("更新用户学习画像成功 - 用户ID: {}, 课程ID: {}, 知识点数: {}", 
                        userId, courseId, masteryData.size());
                } else {
                    log.warn("更新用户学习画像失败 - 用户ID: {}", userId);
                }
            } else {
                // 插入新记录
                UserLearningProfilesEntity newProfile = new UserLearningProfilesEntity();
                newProfile.setUserId(userId);
                newProfile.setCourseId(courseId);
                newProfile.setMasteryLevel(masteryLevelJson);
                newProfile.setLastUpdated(new java.sql.Timestamp(System.currentTimeMillis()));
                
                int inserted = userLearningProfilesMapper.insert(newProfile);
                if (inserted > 0) {
                    log.info("插入用户学习画像成功 - 用户ID: {}, 课程ID: {}, 知识点数: {}", 
                        userId, courseId, masteryData.size());
                } else {
                    log.warn("插入用户学习画像失败 - 用户ID: {}", userId);
                }
            }
            
        } catch (Exception e) {
            log.error("保存用户学习画像失败 - 用户ID: {}, 课程ID: {}", userId, courseId, e);
        }
    }
    
    /**
     * 内部类：封装知识点和其对应的学习记录
     */
    private static class KnowledgePointWithRecords {
        private final KnowledgePointsEntity knowledgePoint;
        private final List<UserLearningRecordsEntity> records;
        
        public KnowledgePointWithRecords(KnowledgePointsEntity knowledgePoint, 
                                          List<UserLearningRecordsEntity> records) {
            this.knowledgePoint = knowledgePoint;
            this.records = records;
        }
        
        public KnowledgePointsEntity getKnowledgePoint() {
            return knowledgePoint;
        }
        
        public List<UserLearningRecordsEntity> getRecords() {
            return records;
        }
        
        public int getRecordCount() {
            return records != null ? records.size() : 0;
        }
    }
    
    /**
     * 将知识点列表分批，每批最多包含maxRecordsPerBatch条答题记录
     * 确保每个知识点的所有记录完整出现在同一批次中
     */
    private List<List<KnowledgePointWithRecords>> createBatches(
            List<KnowledgePointWithRecords> pointsWithRecords, int maxRecordsPerBatch) {
        
        List<List<KnowledgePointWithRecords>> batches = new ArrayList<>();
        List<KnowledgePointWithRecords> currentBatch = new ArrayList<>();
        int currentBatchRecordCount = 0;
        
        for (KnowledgePointWithRecords point : pointsWithRecords) {
            int recordCount = point.getRecordCount();
            
            // 如果当前批次为空，或者加入该知识点后不超过限制，则加入当前批次
            if (currentBatch.isEmpty() || currentBatchRecordCount + recordCount <= maxRecordsPerBatch) {
                currentBatch.add(point);
                currentBatchRecordCount += recordCount;
            } else {
                // 否则，当前批次已满，开始新批次
                batches.add(currentBatch);
                currentBatch = new ArrayList<>();
                currentBatch.add(point);
                currentBatchRecordCount = recordCount;
            }
        }
        
        // 添加最后一个批次
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        
        return batches;
    }
    
    /**
     * 批量分析一批知识点的掌握度
     * @param userId 用户ID
     * @param batch 知识点批次（每个知识点包含其完整的学习记录）
     * @return 掌握度结果Map，key为知识点ID的字符串，value为包含score和comment的Map
     */
    private Map<String, Map<String, Object>> analyzeBatchKnowledgeMasteryWithAI(
            Integer userId, List<KnowledgePointWithRecords> batch) {
        
        Map<String, Map<String, Object>> results = new LinkedHashMap<>();
        
        try {
            // 1. 构建批量分析的提示词
            StringBuilder dataBuilder = new StringBuilder();
            dataBuilder.append("学员ID: ").append(userId).append("\n\n");
            dataBuilder.append("请分析以下知识点的掌握程度：\n\n");
            
            // 2. 遍历每个知识点，组织其答题数据
            for (int i = 0; i < batch.size(); i++) {
                KnowledgePointWithRecords pointWithRecords = batch.get(i);
                KnowledgePointsEntity knowledgePoint = pointWithRecords.getKnowledgePoint();
                List<UserLearningRecordsEntity> records = pointWithRecords.getRecords();
                
                dataBuilder.append("知识点").append(i + 1).append(": ")
                    .append(knowledgePoint.getTitle())
                    .append(" (ID: ").append(knowledgePoint.getPointId()).append(")\n");
                dataBuilder.append("答题记录详情：\n");
                
                // 3. 添加该知识点的所有答题记录
                for (int j = 0; j < records.size(); j++) {
                    UserLearningRecordsEntity record = records.get(j);
                    dataBuilder.append("  第").append(j + 1).append("题:\n");
                    dataBuilder.append("    - 是否正确: ")
                        .append(record.getIsCorrect() ? "正确" : "错误").append("\n");
                    dataBuilder.append("    - 答题时间: ")
                        .append(record.getTimeSpent() != null ? record.getTimeSpent() : 0)
                        .append("秒\n");
                    if (record.getAnsweredAt() != null) {
                        dataBuilder.append("    - 答题时刻: ")
                            .append(record.getAnsweredAt()).append("\n");
                    }
                }
                
                // 4. 计算该知识点的基础统计数据
                long correctCount = records.stream()
                    .filter(record -> record.getIsCorrect() != null && record.getIsCorrect())
                    .count();
                double accuracy = (double) correctCount / records.size() * 100;
                
                int totalTimeSpent = records.stream()
                    .mapToInt(record -> record.getTimeSpent() != null ? record.getTimeSpent() : 0)
                    .sum();
                double avgTimeSpent = (double) totalTimeSpent / records.size();
                
                dataBuilder.append("  统计数据：\n");
                dataBuilder.append("    - 答题总数: ").append(records.size()).append("\n");
                dataBuilder.append("    - 正确数: ").append(correctCount).append("\n");
                dataBuilder.append("    - 正确率: ")
                    .append(String.format("%.2f", accuracy)).append("%\n");
                dataBuilder.append("    - 总答题时长: ").append(totalTimeSpent).append("秒\n");
                dataBuilder.append("    - 平均答题时长: ")
                    .append(String.format("%.2f", avgTimeSpent)).append("秒\n\n");
            }
            
            // 5. 构建火山引擎请求
            VolcEngineTextRequest request = new VolcEngineTextRequest();
            List<VolcEngineTextRequest.Message> messages = new ArrayList<>();
            
            // 系统提示词 - 要求返回JSON格式的批量结果
            String systemPrompt = "你是一位专业的学习分析专家，请根据学员的答题数据，分析该学员对各个知识点的掌握程度。" +
                "请给出每个知识点一个0到1之间的掌握度分数（保留两位小数），其中：" +
                "\n- 0.9-1.0: 完全掌握" +
                "\n- 0.7-0.9: 良好掌握" +
                "\n- 0.5-0.7: 基本掌握" +
                "\n- 0.3-0.5: 部分掌握" +
                "\n- 0.0-0.3: 未掌握" +
                "\n\n评分时请综合考虑：" +
                "\n1. 答题正确率（权重50%）" +
                "\n2. 答题时间（权重30%）- 时间过长或过短都可能说明掌握不牢固" +
                "\n3. 答题趋势（权重20%）- 近期答题表现是否有改善" +
                "\n\n请返回JSON格式的结果，格式为：" +
                "\n{\"知识点ID1\": {\"score\": 掌握度分数, \"comment\": \"评语\"}, \"知识点ID2\": {...}, ...}" +
                "\n注意：只返回JSON对象，不要有任何其他文字。评语不超过30字。";
            
            messages.add(new VolcEngineTextRequest.Message("system", systemPrompt));
            messages.add(new VolcEngineTextRequest.Message("user", dataBuilder.toString()));
            
            request.setModel(volcEngineConfig.getTextModelId());
            request.setMessages(messages);
            request.setTemperature(0.3);
            request.setMaxTokens(1000);
            request.setTopP(0.9);
            
            log.info("准备批量调用火山引擎API分析知识点掌握度，用户ID: {}, 批次包含 {} 个知识点", 
                userId, batch.size());
            
            // 6. 调用火山引擎API
            VolcEngineResponse response = volcEngineService.textInference(request);
            
            // 7. 解析响应
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String aiResult = response.getChoices().get(0).getMessage().getContent().trim();
                log.info("火山引擎AI批量分析结果 - 用户: {}, 批次大小: {}, 原始结果: {}", 
                    userId, batch.size(), aiResult);
                
                // 尝试解析为JSON对象
                try {
                    Map<String, Object> resultMap = objectMapper.readValue(aiResult, Map.class);
                    
                    // 遍历批次中的每个知识点，提取对应的分析结果
                    for (KnowledgePointWithRecords pointWithRecords : batch) {
                        String pointIdStr = String.valueOf(pointWithRecords.getKnowledgePoint().getPointId());
                        
                        if (resultMap.containsKey(pointIdStr)) {
                            Object pointResult = resultMap.get(pointIdStr);
                            
                            if (pointResult instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> pointData = (Map<String, Object>) pointResult;
                                
                                if (pointData.containsKey("score") && pointData.containsKey("comment")) {
                                    Object scoreObj = pointData.get("score");
                                    double masteryScore = scoreObj instanceof Number ? 
                                        ((Number) scoreObj).doubleValue() : 
                                        Double.parseDouble(scoreObj.toString());
                                    
                                    if (masteryScore >= 0 && masteryScore <= 1) {
                                        String comment = pointData.get("comment").toString();
                                        log.info("知识点掌握度分析完成 - 用户: {}, 知识点ID: {}, 掌握度: {}, 评语: {}", 
                                            userId, pointIdStr, masteryScore, comment);
                                        
                                        Map<String, Object> result = new HashMap<>();
                                        result.put("score", masteryScore);
                                        result.put("comment", comment);
                                        results.put(pointIdStr, result);
                                    } else {
                                        log.warn("知识点 {} 的掌握度分数超出范围: {}", pointIdStr, masteryScore);
                                    }
                                } else {
                                    log.warn("知识点 {} 的结果缺少必需字段", pointIdStr);
                                }
                            }
                        } else {
                            log.warn("AI返回结果中缺少知识点 {} 的分析", pointIdStr);
                        }
                    }
                    
                } catch (Exception e) {
                    log.warn("无法解析AI返回的JSON结果: {}", aiResult, e);
                }
            } else {
                log.warn("火山引擎API返回为空或无有效内容");
            }
            
        } catch (Exception e) {
            log.error("批量分析知识点掌握度失败，用户ID: {}", userId, e);
        }
        
        return results;
    }
}
