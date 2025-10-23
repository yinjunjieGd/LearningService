package com.gaodun.learningservice.mapper;

import com.gaodun.learningservice.Entity.UserLearningProgressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户课程学习进度Mapper接口
 * @author shkstart
 */
@Mapper
public interface UserLearningProgressMapper {
    
    /**
     * 根据ID查询学习进度
     */
    UserLearningProgressEntity selectById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询学习进度列表
     */
    List<UserLearningProgressEntity> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 根据课程ID查询学习进度列表
     */
    List<UserLearningProgressEntity> selectByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 根据用户ID和课程ID查询学习进度
     */
    UserLearningProgressEntity selectByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
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
    int delete(@Param("id") Long id);
    
    /**
     * 根据用户ID删除学习进度
     */
    int deleteByUserId(@Param("userId") Long userId);
}
