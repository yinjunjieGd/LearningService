package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.UserLearningProgressEntity;
import com.gaodun.learningservice.manager.UserLearningProgressManager;
import com.gaodun.learningservice.mapper.UserLearningProgressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户课程学习进度Manager实现类
 * @author shkstart
 */
@Service
@Slf4j
public class UserLearningProgressImpl implements UserLearningProgressManager {
    
    @Autowired
    private UserLearningProgressMapper userLearningProgressMapper;
    
    @Override
    public UserLearningProgressEntity selectById(Long id) {
        log.info("Querying user learning progress by id: {}", id);
        return userLearningProgressMapper.selectById(id);
    }
    
    @Override
    public List<UserLearningProgressEntity> selectByUserId(Long userId) {
        log.info("Querying user learning progress by user id: {}", userId);
        return userLearningProgressMapper.selectByUserId(userId);
    }
    
    @Override
    public List<UserLearningProgressEntity> selectByCourseId(Long courseId) {
        log.info("Querying user learning progress by course id: {}", courseId);
        return userLearningProgressMapper.selectByCourseId(courseId);
    }
    
    @Override
    public UserLearningProgressEntity selectByUserIdAndCourseId(Long userId, Long courseId) {
        log.info("Querying user learning progress by user id and course id: userId={}, courseId={}", userId, courseId);
        return userLearningProgressMapper.selectByUserIdAndCourseId(userId, courseId);
    }
    
    @Override
    public List<UserLearningProgressEntity> selectAll() {
        log.info("Querying all user learning progress");
        return userLearningProgressMapper.selectAll();
    }
    
    @Override
    public int insert(UserLearningProgressEntity progress) {
        log.info("Inserting user learning progress: {}", progress);
        return userLearningProgressMapper.insert(progress);
    }
    
    @Override
    public int update(UserLearningProgressEntity progress) {
        log.info("Updating user learning progress: {}", progress);
        return userLearningProgressMapper.update(progress);
    }
    
    @Override
    public int delete(Long id) {
        log.info("Deleting user learning progress by id: {}", id);
        return userLearningProgressMapper.delete(id);
    }
    
    @Override
    public int deleteByUserId(Long userId) {
        log.info("Deleting user learning progress by user id: {}", userId);
        return userLearningProgressMapper.deleteByUserId(userId);
    }
}
