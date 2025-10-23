package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.UserLearningProgressEntity;

import java.util.List;

/**
 * 用户课程学习进度Manager接口
 * @author shkstart
 */
public interface UserLearningProgressManager {
    
    /**
     * 根据ID查询学习进度
     */
    UserLearningProgressEntity selectById(Long id);
    
    /**
     * 根据用户ID查询学习进度列表
     */
    List<UserLearningProgressEntity> selectByUserId(Long userId);
    
    /**
     * 根据课程ID查询学习进度列表
     */
    List<UserLearningProgressEntity> selectByCourseId(Long courseId);
    
    /**
     * 根据用户ID和课程ID查询学习进度
     */
    UserLearningProgressEntity selectByUserIdAndCourseId(Long userId, Long courseId);
    
    /**
     * 查询所有学习进度
     */
    List<UserLearningProgressEntity> selectAll();
    
    /**
     * 插入学习进度
     */
    int insert(UserLearningProgressEntity progress);
    
    /**
     * 更新学习进度
     */
    int update(UserLearningProgressEntity progress);
    
    /**
     * 删除学习进度
     */
    int delete(Long id);
    
    /**
     * 根据用户ID删除学习进度
     */
    int deleteByUserId(Long userId);
}
