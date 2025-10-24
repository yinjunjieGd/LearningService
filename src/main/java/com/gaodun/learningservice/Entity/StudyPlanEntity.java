package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习计划实体类
 * @author shkstart
 */
@Data
@Entity
@Table(name = "study_plan", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id", "knowledge_point"}))
public class StudyPlanEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "course_id", nullable = false)
    private Integer courseId;
    
    @Column(name = "knowledge_point", nullable = false)
    private Integer knowledgePoint;
    
    @Column(name = "study_duration", nullable = false)
    private Double studyDuration;
    
    @Column(name = "learning_order")
    private Integer learningOrder;
    
    @Column(name = "ai_comment", columnDefinition = "text")
    private String aiComment;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
