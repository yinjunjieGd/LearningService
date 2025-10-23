package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.DTO.QuestionDTO;
import com.gaodun.learningservice.Entity.QuestionsEntity;
import com.gaodun.learningservice.manager.QuestionsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 题目Controller
 * @author shkstart
 */
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionsController {
    @Autowired
    private QuestionsManager questionsManager;
    
    // 根据ID查询题目
    @GetMapping("/getById")
    public QuestionsEntity getById(@RequestParam Integer questionId) {
        log.info("Received request to get question by id: {}", questionId);
        return questionsManager.selectById(questionId);
    }
    
    // 查询所有题目
    @GetMapping("/list")
    public List<QuestionsEntity> list() {
        log.info("Received request to list all questions");
        return questionsManager.selectAll();
    }
    
    // 根据课程ID查询题目
    @GetMapping("/listByCourseId")
    public List<QuestionsEntity> listByCourseId(@RequestParam Integer courseId) {
        log.info("Received request to list questions by course id: {}", courseId);
        return questionsManager.selectByCourseId(courseId);
    }
    
    // 根据知识点ID查询题目
    @GetMapping("/listByPointId")
    public List<QuestionsEntity> listByPointId(@RequestParam Integer pointId) {
        log.info("Received request to list questions by point id: {}", pointId);
        return questionsManager.selectByPointId(pointId);
    }
    
    // 根据题目类型查询题目
    @GetMapping("/listByType")
    public List<QuestionsEntity> listByType(@RequestParam String questionType) {
        log.info("Received request to list questions by type: {}", questionType);
        return questionsManager.selectByQuestionType(questionType);
    }
    
    // 根据难度查询题目
    @GetMapping("/listByDifficulty")
    public List<QuestionsEntity> listByDifficulty(@RequestParam String difficulty) {
        log.info("Received request to list questions by difficulty: {}", difficulty);
        return questionsManager.selectByDifficulty(difficulty);
    }
    
    // 插入题目
    @PostMapping("/insert")
    public int insert(@RequestBody QuestionsEntity question) {
        log.info("Received request to insert question: {}", question);
        return questionsManager.insert(question);
    }
    
    // 更新题目
    @PutMapping("/update")
    public int update(@RequestBody QuestionsEntity question) {
        log.info("Received request to update question: {}", question);
        return questionsManager.update(question);
    }
    
    // 删除题目
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer questionId) {
        log.info("Received request to delete question by id: {}", questionId);
        return questionsManager.delete(questionId);
    }
    
    // 根据课程ID随机获取20道题目（包含questionId、stem、type、options、correctAnswer字段）
    @GetMapping("/getQuestionsByCourseId")
    public List<QuestionDTO> getQuestionsByCourseId(@RequestParam Integer courseId) {
        log.info("Received request to get 20 random questions by course id: {}", courseId);
        // 获取指定课程的所有题目
        List<QuestionsEntity> allQuestions = questionsManager.selectByCourseId(courseId);
        
        // 随机打乱题目列表
        Collections.shuffle(allQuestions);
        
        // 限制返回20道题，如果题目总数不足20道则返回全部
        int limit = Math.min(20, allQuestions.size());
        
        // 转换为QuestionDTO列表，包含所有需要的字段
        return IntStream.range(0, limit)
                .mapToObj(i -> {
                    QuestionsEntity question = allQuestions.get(i);
                    QuestionDTO dto = new QuestionDTO();
                    dto.setQuestionId(question.getQuestionId());
                    dto.setStem(question.getStem());
                    dto.setType(question.getType());
                    dto.setOptions(question.getOptionsMap());
                    dto.setCorrectAnswer(question.getCorrectAnswer());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}