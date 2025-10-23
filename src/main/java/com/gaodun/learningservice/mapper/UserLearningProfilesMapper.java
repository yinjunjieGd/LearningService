package com.gaodun.learningservice.mapper;

import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户学习档案Mapper接口
 * @author shkstart
 */
@Mapper
public interface UserLearningProfilesMapper {
    // 根据ID查询用户学习档案
    UserLearningProfilesEntity selectById(Integer profileId);
    
    // 根据用户ID查询学习档案
    List<UserLearningProfilesEntity> selectByUserId(Integer userId);
    
    // 根据用户ID和课程ID查询学习档案
    UserLearningProfilesEntity selectByUserIdAndCourseId(Integer userId, Integer courseId);
    
    // 查询所有学习档案
    List<UserLearningProfilesEntity> selectAll();
    
    // 插入学习档案
    int insert(UserLearningProfilesEntity profile);
    
    // 更新学习档案
    int update(UserLearningProfilesEntity profile);
    
    // 删除学习档案
    int delete(Integer profileId);
}