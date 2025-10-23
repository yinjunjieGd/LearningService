package com.gaodun.learningservice.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 简单的数据清理和课程生成测试类
 * 使用更直接的SQL语句处理数据
 * @author shkstart
 * @create 2025-10-23 4:15
 */
@SpringBootTest
public class SimpleDataCleanupAndCourseGenerator {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void cleanupAndGenerateData() {
        // 首先检查当前数据状态
        checkCurrentDataStatus();

        // 1. 清理知识点表中重复的title数据
        cleanupDuplicateKnowledgePoints();

        // 2. 生成courses表数据
        generateCoursesData();

        // 再次检查处理后的数据状态
        checkCurrentDataStatus();
    }

    /**
     * 检查当前数据状态
     */
    private void checkCurrentDataStatus() {
        System.out.println("\n===== 检查当前数据状态 =====");
        
        try {
            // 检查知识点总数
            String countPointsSql = "SELECT COUNT(*) FROM knowledge_points";
            Integer pointsCount = jdbcTemplate.queryForObject(countPointsSql, Integer.class);
            System.out.println("知识点表总记录数: " + pointsCount);

            // 检查重复的title数量
            String countDuplicateTitlesSql = "SELECT COUNT(DISTINCT title) FROM knowledge_points";
            Integer uniqueTitlesCount = jdbcTemplate.queryForObject(countDuplicateTitlesSql, Integer.class);
            System.out.println("知识点表中唯一title数量: " + uniqueTitlesCount);
            if (pointsCount > uniqueTitlesCount) {
                System.out.println("发现重复title: " + (pointsCount - uniqueTitlesCount) + "个");
            }

            // 检查课程表数据
            String countCoursesSql = "SELECT COUNT(*) FROM courses";
            Integer coursesCount = jdbcTemplate.queryForObject(countCoursesSql, Integer.class);
            System.out.println("courses表记录数: " + coursesCount);

            // 检查知识点表中存在的course_id
            String distinctCourseIdsSql = "SELECT DISTINCT course_id FROM knowledge_points";
            List<Integer> courseIds = jdbcTemplate.queryForList(distinctCourseIdsSql, Integer.class);
            System.out.println("知识点表中的course_id列表: " + courseIds);

        } catch (Exception e) {
            System.err.println("检查数据状态时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 清理知识点表中重复的title数据
     * 优化版本：不再清空prerequisite_id字段，使用更高效的SQL语句直接删除重复数据
     */
    private void cleanupDuplicateKnowledgePoints() {
        System.out.println("\n===== 开始清理知识点表中重复的title数据 =====");

        try {
            // 使用一条SQL语句直接删除重复数据，保留ID最小的记录，同时保留prerequisite_id字段
            String deleteDuplicatesSql = "DELETE FROM knowledge_points WHERE point_id NOT IN ("
                    + "    SELECT min_point_id FROM ("
                    + "        SELECT MIN(point_id) as min_point_id FROM knowledge_points GROUP BY title"
                    + "    ) as unique_points"
                    + ")";
            int deleteCount = jdbcTemplate.update(deleteDuplicatesSql);
            System.out.println("已删除" + deleteCount + "条重复的知识点记录");
            System.out.println("重要：prerequisite_id字段已被保留，不会被清空");

        } catch (Exception e) {
            System.err.println("清理知识点数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成courses表数据
     */
    private void generateCoursesData() {
        System.out.println("\n===== 开始生成courses表数据 =====");

        try {
            // 查询所有不同的course_id
            String queryCourseIdsSql = "SELECT DISTINCT course_id FROM knowledge_points";
            List<Integer> courseIds = jdbcTemplate.queryForList(queryCourseIdsSql, Integer.class);
            System.out.println("找到的course_id列表：" + courseIds);

            // 为每个course_id生成课程数据
            for (Integer courseId : courseIds) {
                // 检查课程是否已存在
                String checkCourseExistsSql = "SELECT COUNT(*) FROM courses WHERE course_id = ?";
                Integer count = jdbcTemplate.queryForObject(checkCourseExistsSql, new Object[]{courseId}, Integer.class);
                
                if (count == 0) {
                    // 获取该课程下的知识点数量
                    String countKnowledgePointsSql = "SELECT COUNT(*) FROM knowledge_points WHERE course_id = ?";
                    Integer kpCount = jdbcTemplate.queryForObject(countKnowledgePointsSql, new Object[]{courseId}, Integer.class);
                    
                    // 根据course_id设置课程标题
                    String title = getCourseTitleByCourseId(courseId);
                    
                    // 设置课程描述
                    String description = "本课程包含" + kpCount + "个知识点，涵盖了" + title + "的核心内容。";
                    
                    // 根据课程ID设置科目和难度
                    String subject = "通用课程";
                    String difficulty = "intermediate";
                    
                    if (courseId == 1) {
                        subject = "高等数学";
                        difficulty = "advanced";
                    } else if (courseId == 4) {
                        subject = "会计";
                        difficulty = "intermediate";
                    }
                    
                    // 插入课程数据 - 使用正确的表结构，移除不存在的create_time和update_time
                    String insertCourseSql = "INSERT INTO courses (course_id, teacher_id, title, description, subject, difficulty) VALUES (?, ?, ?, ?, ?, ?)";
                    jdbcTemplate.update(insertCourseSql, courseId, 1001, title, description, subject, difficulty);
                    System.out.println("已生成课程：course_id=" + courseId + ", title=" + title);
                } else {
                    System.out.println("课程已存在：course_id=" + courseId);
                }
            }
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