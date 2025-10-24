package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 用户学习画像实体类
 * @author shkstart
 */
@Data
@Entity
@Table(name = "user_learning_profiles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class UserLearningProfilesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer profileId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "course_id", nullable = false)
    private Integer courseId;
    
    /**
     * 知识点掌握度(JSON格式)
     * 格式: {"知识点id": {"score": 0.85, "comment": "掌握良好"}, ...}
     */
    @Column(name = "mastery_level", columnDefinition = "text")
    private String masteryLevel;
    
    @Column(name = "preferred_style", length = 50)
    private String preferredStyle;
    
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
