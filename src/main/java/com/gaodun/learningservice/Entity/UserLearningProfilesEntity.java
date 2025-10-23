package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户学习档案实体类
 * @author shkstart
 */
@Data
@Entity
@Table(name = "user_learning_profiles")
public class UserLearningProfilesEntity {
    @Id
    @Column(name = "profile_id")
    private Integer profileId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "course_id")
    private Integer courseId;
    
    @Column(name = "current_progress")
    private Double currentProgress;
    
    @Column(name = "last_study_time")
    private java.util.Date lastStudyTime;
    
    @Column(name = "total_study_duration")
    private Integer totalStudyDuration;
    
    @Column(name = "completion_status")
    private String completionStatus;
    
    @Column(name = "test_score")
    private Integer testScore;
}