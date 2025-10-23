package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户学习记录实体类
 * @author shkstart
 */
@Data
@Entity
@Table(name = "user_learning_records")
public class UserLearningRecordsEntity {
    @Id
    @Column(name = "record_id")
    private Integer recordId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "question_id")
    private Integer questionId;
    
    @Column(name = "course_id")
    private Integer courseId;
    
    @Column(name = "user_answer")
    private String userAnswer;
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    @Column(name = "answer_time")
    private java.util.Date answerTime;
    
    @Column(name = "spend_time")
    private Integer spendTime;
}