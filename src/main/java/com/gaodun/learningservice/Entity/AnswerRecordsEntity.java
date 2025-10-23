package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户答题记录实体类
 * @author shkstart
 */
@Data
@Entity
@Table(name = "answer_records")
public class AnswerRecordsEntity {
    @Id
    @Column(name = "record_id")
    private Integer recordId;
    
    @Column(name = "exam_name")
    private String examName;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "start_time")
    private Date startTime;
    
    @Column(name = "end_time")
    private Date endTime;
    
    @Column(name = "time_spent")
    private Integer timeSpent;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "score")
    private BigDecimal score;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
}
