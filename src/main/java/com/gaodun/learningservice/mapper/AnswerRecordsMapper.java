package com.gaodun.learningservice.mapper;

import com.gaodun.learningservice.Entity.AnswerRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户答题记录Mapper接口
 * @author shkstart
 */
@Mapper
public interface AnswerRecordsMapper {
    // 根据ID查询答题记录
    AnswerRecordsEntity selectById(Integer recordId);
    
    // 根据用户ID查询答题记录
    List<AnswerRecordsEntity> selectByUserId(Integer userId);
    
    // 根据考试名称查询答题记录
    List<AnswerRecordsEntity> selectByExamName(String examName);
    
    // 根据答题状态查询答题记录
    List<AnswerRecordsEntity> selectByStatus(Integer status);
    
    // 查询所有答题记录
    List<AnswerRecordsEntity> selectAll();
    
    // 插入答题记录
    int insert(AnswerRecordsEntity record);
    
    // 更新答题记录
    int update(AnswerRecordsEntity record);
    
    // 删除答题记录
    int delete(Integer recordId);
}
