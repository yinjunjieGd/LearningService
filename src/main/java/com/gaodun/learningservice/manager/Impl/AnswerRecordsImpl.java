package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.AnswerRecordsEntity;
import com.gaodun.learningservice.manager.AnswerRecordsManager;
import com.gaodun.learningservice.mapper.AnswerRecordsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户答题记录Manager实现类
 * @author shkstart
 */
@Slf4j
@Service
public class AnswerRecordsImpl implements AnswerRecordsManager {
    @Autowired
    private AnswerRecordsMapper answerRecordsMapper;
    
    @Override
    public AnswerRecordsEntity selectById(Integer recordId) {
        log.info("selectById: {}", recordId);
        return answerRecordsMapper.selectById(recordId);
    }
    
    @Override
    public List<AnswerRecordsEntity> selectByUserId(Integer userId) {
        log.info("selectByUserId: {}", userId);
        return answerRecordsMapper.selectByUserId(userId);
    }
    
    @Override
    public List<AnswerRecordsEntity> selectByExamName(String examName) {
        log.info("selectByExamName: {}", examName);
        return answerRecordsMapper.selectByExamName(examName);
    }
    
    @Override
    public List<AnswerRecordsEntity> selectByStatus(Integer status) {
        log.info("selectByStatus: {}", status);
        return answerRecordsMapper.selectByStatus(status);
    }
    
    @Override
    public List<AnswerRecordsEntity> selectAll() {
        log.info("selectAll");
        return answerRecordsMapper.selectAll();
    }
    
    @Override
    public AnswerRecordsEntity insert(AnswerRecordsEntity record) {
        log.info("insert: {}", record);
        answerRecordsMapper.insert(record);
        log.info("insert success, recordId: {}", record.getRecordId());
        return record;
    }
    
    @Override
    public int update(AnswerRecordsEntity record) {
        log.info("update: {}", record);
        return answerRecordsMapper.update(record);
    }
    
    @Override
    public int delete(Integer recordId) {
        log.info("delete: {}", recordId);
        return answerRecordsMapper.delete(recordId);
    }
}
