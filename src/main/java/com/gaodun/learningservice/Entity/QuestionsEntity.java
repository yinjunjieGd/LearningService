package com.gaodun.learningservice.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 题目实体类
 * 与questions表结构保持一致
 * @author shkstart
 */
@Data
@Entity
@Table(name = "questions")
public class QuestionsEntity {
    @Id
    @Column(name = "question_id")
    private Integer questionId;
    
    @Column(name = "point_id")
    private Integer pointId;
    
    @Column(name = "stem")
    private String stem;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "options")
    private String options;
    
    /**
     * 将Map类型的options转换为JSON字符串
     */
    public void setOptionsMap(Map<String, String> optionsMap) {
        try {
            this.options = new ObjectMapper().writeValueAsString(optionsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert options map to JSON", e);
        }
    }
    
    /**
     * 将JSON字符串转换为Map类型的options
     */
    @SneakyThrows
    public Map<String, String> getOptionsMap() {
        if (this.options == null) {
            return new HashMap<>();
        }
        return new ObjectMapper().readValue(this.options, Map.class);
    }
    
    @Column(name = "correct_answer")
    private String correctAnswer;
    
    @Column(name = "ai_analysis")
    private String aiAnalysis;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
}