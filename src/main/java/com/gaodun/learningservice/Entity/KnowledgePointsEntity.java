package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 知识点实体类
 * @author shkstart
 * @create 2025-10-23 2:30
 */
@Data
@Entity
@Table(name = "knowledge_points")
public class KnowledgePointsEntity {
    @Id
    @Column(name = "point_id")
    private Integer pointId;
    
    @Column(name = "course_id")
    private Integer courseId;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "prerequisite_id")
    private Integer prerequisiteId;
}