package com.gaodun.learningservice.mapper;

import com.gaodun.learningservice.Entity.StudyPlanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习计划Mapper接口
 * @author shkstart
 */
@Mapper
public interface StudyPlanMapper {
    
    /**
     * 根据用户ID和课程ID查询学习计划列表
     */
    List<StudyPlanEntity> selectByUserIdAndCourseId(@Param("userId") Integer userId, 
                                                     @Param("courseId") Integer courseId);
    
    /**
     * 根据用户ID、课程ID和知识点查询学习计划
     */
    StudyPlanEntity selectByUserIdAndCourseIdAndKnowledgePoint(@Param("userId") Integer userId,
                                                                @Param("courseId") Integer courseId,
                                                                @Param("knowledgePoint") String knowledgePoint);
    
    /**
     * 插入学习计划
     */
    int insert(StudyPlanEntity studyPlan);
    
    /**
     * 更新学习计划
     */
    int update(StudyPlanEntity studyPlan);
    
    /**
     * 批量插入学习计划
     */
    int batchInsert(@Param("list") List<StudyPlanEntity> studyPlans);
    
    /**
     * 根据用户ID和课程ID删除学习计划
     */
    int deleteByUserIdAndCourseId(@Param("userId") Integer userId, 
                                   @Param("courseId") Integer courseId);
}
