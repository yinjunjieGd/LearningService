package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.UserLearningRecordsEntity;

import java.util.List;

/**
 * 用户学习记录Manager接口
 * @author shkstart
 */
public interface UserLearningRecordsManager {
    // 根据ID查询用户学习记录
    UserLearningRecordsEntity selectById(Integer recordId);
    
    // 根据用户ID查询学习记录
    List<UserLearningRecordsEntity> selectByUserId(Integer userId);
    
    // 根据课程ID查询学习记录
    List<UserLearningRecordsEntity> selectByCourseId(Integer courseId);
    
    // 根据题目ID查询学习记录
    List<UserLearningRecordsEntity> selectByQuestionId(Integer questionId);
    
    // 查询所有学习记录
    List<UserLearningRecordsEntity> selectAll();
    
    // 插入学习记录
    int insert(UserLearningRecordsEntity record);
    
    // 更新学习记录
    int update(UserLearningRecordsEntity record);
    
    // 删除学习记录
    int delete(Integer recordId);
}