package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.Entity.QuestionsEntity;
import com.gaodun.learningservice.manager.QuestionsManager;
import com.gaodun.learningservice.mapper.KnowledgePointsMapper;
import com.gaodun.learningservice.mapper.QuestionsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目Manager实现类
 * @author shkstart
 */
@Slf4j
@Service
public class QuestionsImpl implements QuestionsManager {
    @Autowired
    private QuestionsMapper questionsMapper;
    
    @Override
    public QuestionsEntity selectById(Integer questionId) {
        log.info("selectById: {}", questionId);
        return questionsMapper.selectById(questionId);
    }
    
    @Override
    public List<QuestionsEntity> selectAll() {
        log.info("selectAll");
        return questionsMapper.selectAll();
    }
    
    @Autowired
    private KnowledgePointsMapper knowledgePointsMapper;
    
    @Override
    public List<QuestionsEntity> selectByCourseId(Integer courseId) {
        log.info("selectByCourseId: {}", courseId);
        // 获取指定课程的所有知识点
        List<KnowledgePointsEntity> knowledgePoints = knowledgePointsMapper.selectByCourseId(courseId);
        if (knowledgePoints.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取所有知识点ID
        List<Integer> pointIds = knowledgePoints.stream()
                .map(KnowledgePointsEntity::getPointId)
                .collect(Collectors.toList());
        
        // 通过知识点ID列表查询题目
        return questionsMapper.selectByPointIds(pointIds);
    }
    
    @Override
    public List<QuestionsEntity> selectByPointId(Integer pointId) {
        log.info("selectByPointId: {}", pointId);
        return questionsMapper.selectByPointId(pointId);
    }
    
    @Override
    public List<QuestionsEntity> selectByQuestionType(String questionType) {
        log.info("selectByQuestionType: {}", questionType);
        return questionsMapper.selectByType(questionType);
    }

    @Override
    public List<QuestionsEntity> selectByDifficulty(String difficulty) {
        log.info("selectByDifficulty: {}", difficulty);
        // QuestionsMapper中已移除selectByDifficulty方法，返回空列表
        return new ArrayList<>();
    }
    
    @Override
    public int insert(QuestionsEntity question) {
        log.info("insert: {}", question);
        return questionsMapper.insert(question);
    }
    
    @Override
    public int update(QuestionsEntity question) {
        log.info("update: {}", question);
        return questionsMapper.update(question);
    }
    
    @Override
    public int delete(Integer questionId) {
        log.info("delete: {}", questionId);
        return questionsMapper.delete(questionId);
    }
}