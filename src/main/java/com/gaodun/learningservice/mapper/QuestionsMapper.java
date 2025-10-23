package com.gaodun.learningservice.mapper;

import com.gaodun.learningservice.Entity.QuestionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目Mapper接口
 * 与questions表结构保持一致
 * @author shkstart
 */
@Mapper
public interface QuestionsMapper {
    // 根据ID查询题目
    QuestionsEntity selectById(Integer questionId);
    
    // 查询所有题目
    List<QuestionsEntity> selectAll();
    
    // 根据知识点ID查询题目
    List<QuestionsEntity> selectByPointId(Integer pointId);
    
    // 根据题目类型查询题目
    List<QuestionsEntity> selectByType(String type);
    
    // 插入题目
    int insert(QuestionsEntity question);
    
    // 更新题目
    int update(QuestionsEntity question);
    
    // 删除题目
    int delete(Integer questionId);
    
    // 根据知识点ID列表查询题目
    List<QuestionsEntity> selectByPointIds(@Param("pointIds") List<Integer> pointIds);
    
    // 查询指定知识点的题目数量
    int countByPointId(Integer pointId);
}