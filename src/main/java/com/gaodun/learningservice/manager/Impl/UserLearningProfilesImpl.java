package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;
import com.gaodun.learningservice.manager.UserLearningProfilesManager;
import com.gaodun.learningservice.mapper.UserLearningProfilesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户学习画像Manager实现类
 * @author shkstart
 */
@Slf4j
@Service
public class UserLearningProfilesImpl implements UserLearningProfilesManager {
    @Autowired
    private UserLearningProfilesMapper userLearningProfilesMapper;
    
    @Override
    public UserLearningProfilesEntity selectById(Integer profileId) {
        log.info("selectById: {}", profileId);
        return userLearningProfilesMapper.selectById(profileId);
    }
    
    @Override
    public UserLearningProfilesEntity selectByUserId(Integer userId) {
        log.info("selectByUserId: {}", userId);
        return userLearningProfilesMapper.selectByUserId(userId);
    }
    
    @Override
    public List<UserLearningProfilesEntity> selectAll() {
        log.info("selectAll");
        return userLearningProfilesMapper.selectAll();
    }
    
    @Override
    public int insert(UserLearningProfilesEntity profile) {
        log.info("insert: {}", profile);
        return userLearningProfilesMapper.insert(profile);
    }
    
    @Override
    public int update(UserLearningProfilesEntity profile) {
        log.info("update: {}", profile);
        return userLearningProfilesMapper.update(profile);
    }
    
    @Override
    public int delete(Integer profileId) {
        log.info("delete: {}", profileId);
        return userLearningProfilesMapper.delete(profileId);
    }
    
    @Override
    public int deleteByUserId(Integer userId) {
        log.info("deleteByUserId: {}", userId);
        return userLearningProfilesMapper.deleteByUserId(userId);
    }
}
