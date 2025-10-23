package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.UserLearningRecordsEntity;
import com.gaodun.learningservice.manager.UserLearningRecordsManager;
import com.gaodun.learningservice.mapper.UserLearningRecordsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户学习记录Manager实现类
 * @author shkstart
 */
@Slf4j
@Service
public class UserLearningRecordsImpl implements UserLearningRecordsManager {
    @Autowired
    private UserLearningRecordsMapper userLearningRecordsMapper;
    
    @Override
    public UserLearningRecordsEntity selectById(Integer recordId) {
        log.info("selectById: {}", recordId);
        return userLearningRecordsMapper.selectById(recordId);
    }
    
    @Override
    public List<UserLearningRecordsEntity> selectByUserId(Integer userId) {
        log.info("selectByUserId: {}", userId);
        return userLearningRecordsMapper.selectByUserId(userId);
    }
    
    @Override
    public List<UserLearningRecordsEntity> selectByCourseId(Integer courseId) {
        log.info("selectByCourseId: {}", courseId);
        return userLearningRecordsMapper.selectByCourseId(courseId);
    }
    
    @Override
    public List<UserLearningRecordsEntity> selectByQuestionId(Integer questionId) {
        log.info("selectByQuestionId: {}", questionId);
        return userLearningRecordsMapper.selectByQuestionId(questionId);
    }
    
    @Override
    public List<UserLearningRecordsEntity> selectAll() {
        log.info("selectAll");
        return userLearningRecordsMapper.selectAll();
    }
    
    @Override
    public int insert(UserLearningRecordsEntity record) {
        log.info("insert: {}", record);
        return userLearningRecordsMapper.insert(record);
    }
    
    @Override
    public int update(UserLearningRecordsEntity record) {
        log.info("update: {}", record);
        return userLearningRecordsMapper.update(record);
    }
    
    @Override
    public int delete(Integer recordId) {
        log.info("delete: {}", recordId);
        return userLearningRecordsMapper.delete(recordId);
    }
}