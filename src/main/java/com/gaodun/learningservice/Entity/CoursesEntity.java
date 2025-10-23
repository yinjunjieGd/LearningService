package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author shkstart
 * @create 2025-10-22 23:58
 */
@Data
@Entity
@Table(name = "courses")
public class CoursesEntity{
    @Id
    @Column(name = "course_id")
    private Integer id;
    
    @Column(name = "teacher_id")
    private Integer teacherId;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "create_time")
    private java.util.Date createTime;
    
    @Column(name = "update_time")
    private java.util.Date updateTime;

}
