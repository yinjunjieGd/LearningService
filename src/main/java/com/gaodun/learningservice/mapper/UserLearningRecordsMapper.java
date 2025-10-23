package com.gaodun.learningservice.mapper;

import com.gaodun.learningservice.Entity.UserLearningRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户学习记录Mapper接口
 * @author shkstart
 */
@Mapper
public interface UserLearningRecordsMapper {
    // 根据ID查询用户学习记录
    UserLearningRecordsEntity selectById(Integer recordId);
    
    // 根据用户ID查询学习记录
    List<UserLearningRecordsEntity> selectByUserId(Integer userId);
    
    // 根据题目ID查询学习记录
    List<UserLearningRecordsEntity> selectByQuestionId(Integer questionId);
    
    // 根据答题记录ID查询学习记录
    List<UserLearningRecordsEntity> selectByAnswerId(Integer answerId);
    
    // 根据用户ID和知识点ID查询前10条学习记录
    List<UserLearningRecordsEntity> selectTop10ByUserIdAndPointId(Integer userId, Integer pointId);
    
    // 查询所有学习记录
    List<UserLearningRecordsEntity> selectAll();
    
    // 插入学习记录
    int insert(UserLearningRecordsEntity record);
    
    // 更新学习记录
    int update(UserLearningRecordsEntity record);
    
    // 删除学习记录
    int delete(Integer recordId);
}