package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.manager.KnowledgePointsManager;
import com.gaodun.learningservice.mapper.KnowledgePointsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 知识点Manager实现类
 * @author shkstart
 * @create 2025-10-23 2:36
 */
@Slf4j
@Service
public class KnowledgePointsImpl implements KnowledgePointsManager {
    @Autowired
    private KnowledgePointsMapper knowledgePointsMapper;
    
    @Override
    public KnowledgePointsEntity selectById(Integer pointId) {
        log.info("selectById: {}", pointId);
        return knowledgePointsMapper.selectById(pointId);
    }
    
    @Override
    public List<KnowledgePointsEntity> selectAll() {
        log.info("selectAll");
        return knowledgePointsMapper.selectAll();
    }
    
    @Override
    public List<KnowledgePointsEntity> selectByCourseId(Integer courseId) {
        log.info("selectByCourseId: {}", courseId);
        return knowledgePointsMapper.selectByCourseId(courseId);
    }
    
    @Override
    public int insert(KnowledgePointsEntity knowledgePoint) {
        log.info("insert: {}", knowledgePoint);
        return knowledgePointsMapper.insert(knowledgePoint);
    }
    
    @Override
    public int update(KnowledgePointsEntity knowledgePoint) {
        log.info("update: {}", knowledgePoint);
        return knowledgePointsMapper.update(knowledgePoint);
    }
    
    @Override
    public int delete(Integer pointId) {
        log.info("delete: {}", pointId);
        return knowledgePointsMapper.delete(pointId);
    }
}