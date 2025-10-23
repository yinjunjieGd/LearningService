package com.gaodun.learningservice.DTO;

import lombok.Data;

import java.util.Map;

/**
 * 题目DTO类,用于封装返回给前端的题目数据
 * 包含字段:questionId、stem、type、options、correctAnswer
 */
@Data
public class QuestionDTO {
    private Integer questionId;
    private String stem;
    private String type;
    private Map<String, String> options;
    private String correctAnswer;
}