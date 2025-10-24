package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.StudyPlanEntity;

import java.util.List;

/**
 * 学习计划Manager接口
 * @author shkstart
 */
public interface StudyPlanManager {
    
    /**
     * 生成学习计划
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 生成的学习计划列表
     */
    List<StudyPlanEntity> generateStudyPlan(Integer userId, Integer courseId);
    
    /**
     * 根据用户ID和课程ID查询学习计划列表
     */
    List<StudyPlanEntity> selectByUserIdAndCourseId(Integer userId, Integer courseId);
    
    /**
     * 更新或创建学习计划
     */
    int saveOrUpdate(StudyPlanEntity studyPlan);
}
