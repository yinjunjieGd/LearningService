package com.gaodun.learningservice.DTO;

import com.gaodun.learningservice.Entity.StudyPlanEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 学习计划DTO，包含知识点名称
 */
@Data
@NoArgsConstructor
public class StudyPlanDTO {
    private Long planId;
    private Integer userId;
    private Integer courseId;
    private Integer knowledgePoint;
    private String knowledgePointName; // 知识点名称
    private Double studyDuration;
    private Integer learningOrder;
    private String aiComment;
    private Date createdAt;
    private Date updatedAt;
    
    /**
     * 从StudyPlanEntity转换为StudyPlanDTO
     */
    public StudyPlanDTO(StudyPlanEntity entity) {
        this.planId = entity.getPlanId();
        this.userId = entity.getUserId();
        this.courseId = entity.getCourseId();
        this.knowledgePoint = entity.getKnowledgePoint();
        this.studyDuration = entity.getStudyDuration();
        this.learningOrder = entity.getLearningOrder();
        this.aiComment = entity.getAiComment();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}