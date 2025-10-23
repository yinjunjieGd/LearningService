package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.KnowledgePointsEntity;

import java.util.List;

/**
 * 知识点Manager接口
 * @author shkstart
 * @create 2025-10-23 2:34
 */
public interface KnowledgePointsManager {
    // 根据ID查询知识点
    KnowledgePointsEntity selectById(Integer pointId);
    
    // 查询所有知识点
    List<KnowledgePointsEntity> selectAll();
    
    // 根据课程ID查询知识点
    List<KnowledgePointsEntity> selectByCourseId(Integer courseId);
    
    // 插入知识点
    int insert(KnowledgePointsEntity knowledgePoint);
    
    // 更新知识点
    int update(KnowledgePointsEntity knowledgePoint);
    
    // 删除知识点
    int delete(Integer pointId);
}