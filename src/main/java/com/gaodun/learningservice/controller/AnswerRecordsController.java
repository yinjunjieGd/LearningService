package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.AnswerRecordsEntity;
import com.gaodun.learningservice.manager.AnswerRecordsManager;
import com.gaodun.learningservice.service.AnswerRecordAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户答题记录Controller
 * @author shkstart
 */
@RestController
@RequestMapping("/api/answer-records")
@Slf4j
public class AnswerRecordsController {
    @Autowired
    private AnswerRecordsManager answerRecordsManager;
    
    @Autowired
    private AnswerRecordAsyncService answerRecordAsyncService;
    
    // 根据ID查询答题记录
    @GetMapping("/getById")
    public AnswerRecordsEntity getById(@RequestParam Integer recordId) {
        log.info("Received request to get answer record by id: {}", recordId);
        return answerRecordsManager.selectById(recordId);
    }
    
    // 根据用户ID查询答题记录
    @GetMapping("/listByUserId")
    public List<AnswerRecordsEntity> listByUserId(@RequestParam Integer userId) {
        log.info("Received request to list answer records by user id: {}", userId);
        return answerRecordsManager.selectByUserId(userId);
    }
    
    // 根据考试名称查询答题记录
    @GetMapping("/listByExamName")
    public List<AnswerRecordsEntity> listByExamName(@RequestParam String examName) {
        log.info("Received request to list answer records by exam name: {}", examName);
        return answerRecordsManager.selectByExamName(examName);
    }
    
    // 根据答题状态查询答题记录
    @GetMapping("/listByStatus")
    public List<AnswerRecordsEntity> listByStatus(@RequestParam Integer status) {
        log.info("Received request to list answer records by status: {}", status);
        return answerRecordsManager.selectByStatus(status);
    }
    
    // 查询所有答题记录
    @GetMapping("/list")
    public List<AnswerRecordsEntity> list() {
        log.info("Received request to list all answer records");
        return answerRecordsManager.selectAll();
    }
    
    // 插入答题记录
    @PostMapping("/insert")
    public AnswerRecordsEntity insert(@RequestBody AnswerRecordsEntity record) {
        log.info("Received request to insert answer record: {}", record);
        return answerRecordsManager.insert(record);
    }
    
    // 更新答题记录
    @PutMapping("/update")
    public int update(@RequestBody AnswerRecordsEntity record) {
        log.info("Received request to update answer record: {}", record);
        int result = answerRecordsManager.update(record);
        
        // 如果更新成功，触发异步任务
        if (result > 0) {
            log.info("Answer record updated successfully, triggering async task for record: {}", record.getRecordId());
            answerRecordAsyncService.processAnswerRecordUpdate(record.getRecordId());
        }
        
        return result;
    }
    
    // 删除答题记录
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer recordId) {
        log.info("Received request to delete answer record by id: {}", recordId);
        return answerRecordsManager.delete(recordId);
    }
}
