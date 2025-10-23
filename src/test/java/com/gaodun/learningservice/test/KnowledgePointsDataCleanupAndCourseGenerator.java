package com.gaodun.learningservice.test;

import com.gaodun.learningservice.Entity.CoursesEntity;
import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.mapper.CoursesMapper;
import com.gaodun.learningservice.mapper.KnowledgePointsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * 知识点数据清理和课程数据生成工具类
 * 1. 删除知识点表中title相同的数据，只保留一条
 * 2. 根据当前所有知识点数据生成courses表的数据
 * @author shkstart
 * @create 2025-10-23 3:50
 */
@SpringBootTest
public class KnowledgePointsDataCleanupAndCourseGenerator {

    @Autowired
    private KnowledgePointsMapper knowledgePointsMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void cleanupDuplicateKnowledgePointsAndGenerateCourses() {
        // 1. 清理知识点表中重复的title数据
        cleanupDuplicateKnowledgePoints();

        // 2. 根据知识点数据生成courses表数据
        generateCoursesFromKnowledgePoints();
    }

    /**
     * 清理知识点表中重复的title数据，只保留一条
     */
    private void cleanupDuplicateKnowledgePoints() {
        System.out.println("开始清理知识点表中重复的title数据...");

        try {
            // 使用SQL语句直接删除重复数据，保留ID最小的记录
            String sql = "DELETE FROM knowledge_points WHERE point_id NOT IN (" +
                         "    SELECT min_point_id FROM (" +
                         "        SELECT MIN(point_id) as min_point_id FROM knowledge_points GROUP BY title" +
                         "    ) as unique_points" +
                         ")";
            
            int deleteCount = jdbcTemplate.update(sql);
            
            System.out.println("知识点数据清理完成，共删除了" + deleteCount + "条重复数据");
        } catch (Exception e) {
            System.err.println("清理知识点数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据知识点数据生成courses表数据
     */
    private void generateCoursesFromKnowledgePoints() {
        System.out.println("开始根据知识点数据生成courses表数据...");

        try {
            // 获取所有唯一的courseId
            String sqlGetCourseIds = "SELECT DISTINCT course_id FROM knowledge_points";
            List<Integer> courseIds = jdbcTemplate.queryForList(sqlGetCourseIds, Integer.class);

            System.out.println("找到" + courseIds.size() + "个不同的课程ID: " + courseIds);

            // 为每个课程ID生成课程数据
            int insertCount = 0;
            for (Integer courseId : courseIds) {
                // 检查课程是否已存在
                String sqlCheckExist = "SELECT COUNT(*) FROM courses WHERE course_id = ?";
                Integer count = jdbcTemplate.queryForObject(sqlCheckExist, Integer.class, courseId);
                
                if (count == 0) {
                    // 创建新课程数据
                    String courseTitle = getCourseTitleByCourseId(courseId);
                    
                    // 获取该课程下的知识点数量
                    String sqlGetPointCount = "SELECT COUNT(*) FROM knowledge_points WHERE course_id = ?";
                    int pointCount = jdbcTemplate.queryForObject(sqlGetPointCount, Integer.class, courseId);
                    
                    // 生成课程描述
                    String description = courseTitle + "课程，包含" + pointCount + "个知识点";
                    
                    // 插入课程数据
                    String sqlInsertCourse = "INSERT INTO courses (course_id, teacher_id, title, description, create_time, update_time) VALUES (?, ?, ?, ?, NOW(), NOW())";
                    int result = jdbcTemplate.update(sqlInsertCourse, 
                            courseId,
                            1, // 默认教师ID为1
                            courseTitle,
                            description
                    );
                    
                    if (result > 0) {
                        insertCount++;
                        System.out.println("已生成课程: " + courseTitle + " (ID: " + courseId + ")");
                    }
                } else {
                    System.out.println("课程已存在，跳过生成: ID " + courseId);
                }
            }

            System.out.println("课程数据生成完成，共插入了" + insertCount + "条课程数据");
        } catch (Exception e) {
            System.err.println("生成课程数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据courseId获取课程标题
     */
    private String getCourseTitleByCourseId(Integer courseId) {
        switch (courseId) {
            case 1:
                return "高等数学(微积分)";
            case 2:
                return "线性代数";
            case 3:
                return "概率论与数理统计";
            case 4:
                return "初级会计实务";
            default:
                return "课程" + courseId;
        }
    }
}