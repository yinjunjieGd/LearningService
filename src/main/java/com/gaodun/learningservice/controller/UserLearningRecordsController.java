package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.UserLearningRecordsEntity;
import com.gaodun.learningservice.manager.UserLearningRecordsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户学习记录Controller
 * @author shkstart
 */
@RestController
@RequestMapping("/api/user-learning-records")
@RequiredArgsConstructor
@Slf4j
public class UserLearningRecordsController {
    @Autowired
    private UserLearningRecordsManager userLearningRecordsManager;
    
    // 根据ID查询用户学习记录
    @GetMapping("/getById")
    public UserLearningRecordsEntity getById(@RequestParam Integer recordId) {
        log.info("Received request to get user learning record by id: {}", recordId);
        return userLearningRecordsManager.selectById(recordId);
    }
    
    // 根据用户ID查询学习记录
    @GetMapping("/listByUserId")
    public List<UserLearningRecordsEntity> listByUserId(@RequestParam Integer userId) {
        log.info("Received request to list user learning records by user id: {}", userId);
        return userLearningRecordsManager.selectByUserId(userId);
    }
    
    // 根据课程ID查询学习记录
    // 根据题目ID查询学习记录
    @GetMapping("/listByQuestionId")
    public List<UserLearningRecordsEntity> listByQuestionId(@RequestParam Integer questionId) {
        log.info("Received request to list user learning records by question id: {}", questionId);
        return userLearningRecordsManager.selectByQuestionId(questionId);
    }
    
    // 查询所有学习记录
    @GetMapping("/list")
    public List<UserLearningRecordsEntity> list() {
        log.info("Received request to list all user learning records");
        return userLearningRecordsManager.selectAll();
    }
    
    // 插入学习记录
    @PostMapping("/insert")
    public int insert(@RequestBody UserLearningRecordsEntity record) {
        log.info("Received request to insert user learning record: {}", record);
        return userLearningRecordsManager.insert(record);
    }
    
    // 更新学习记录
    @PutMapping("/update")
    public int update(@RequestBody UserLearningRecordsEntity record) {
        log.info("Received request to update user learning record: {}", record);
        return userLearningRecordsManager.update(record);
    }
    
    // 删除学习记录
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer recordId) {
        log.info("Received request to delete user learning record by id: {}", recordId);
        return userLearningRecordsManager.delete(recordId);
    }
}