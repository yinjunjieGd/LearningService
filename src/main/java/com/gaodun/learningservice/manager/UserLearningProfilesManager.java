package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;

import java.util.List;

/**
 * 用户学习画像Manager接口
 * @author shkstart
 */
public interface UserLearningProfilesManager {
    // 根据profile_id查询用户学习画像
    UserLearningProfilesEntity selectById(Integer profileId);
    
    // 根据用户ID查询学习画像（user_id有唯一约束，返回单个对象）
    UserLearningProfilesEntity selectByUserId(Integer userId);
    
    // 查询所有学习画像
    List<UserLearningProfilesEntity> selectAll();
    
    // 插入学习画像
    int insert(UserLearningProfilesEntity profile);
    
    // 更新学习画像
    int update(UserLearningProfilesEntity profile);
    
    // 删除学习画像
    int delete(Integer profileId);
    
    // 根据用户ID删除学习画像
    int deleteByUserId(Integer userId);
}
