package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.QuestionsEntity;

import java.util.List;

/**
 * 题目Manager接口
 * @author shkstart
 */
public interface QuestionsManager {
    // 根据ID查询题目
    QuestionsEntity selectById(Integer questionId);
    
    // 查询所有题目
    List<QuestionsEntity> selectAll();
    
    // 根据课程ID查询题目
    List<QuestionsEntity> selectByCourseId(Integer courseId);
    
    // 根据知识点ID查询题目
    List<QuestionsEntity> selectByPointId(Integer pointId);
    
    // 根据题目类型查询题目
    List<QuestionsEntity> selectByQuestionType(String questionType);
    
    // 根据难度查询题目
    List<QuestionsEntity> selectByDifficulty(String difficulty);
    
    // 插入题目
    int insert(QuestionsEntity question);
    
    // 更新题目
    int update(QuestionsEntity question);
    
    // 删除题目
    int delete(Integer questionId);
}