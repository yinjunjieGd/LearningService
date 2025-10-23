package com.gaodun.learningservice.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 数据验证测试类
 * 验证知识点数据清理和课程生成的结果
 * @author shkstart
 * @create 2025-10-23 4:00
 */
@SpringBootTest
public class DataVerificationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void verifyKnowledgePointsAndCoursesData() {
        // 1. 验证知识点表中是否还有重复的title
        System.out.println("\n===== 验证知识点表数据 =====");
        verifyKnowledgePointsNoDuplicates();

        // 2. 验证courses表是否已生成数据
        System.out.println("\n===== 验证课程表数据 =====");
        verifyCoursesDataGenerated();
    }

    /**
     * 验证知识点表中是否还有重复的title
     */
    private void verifyKnowledgePointsNoDuplicates() {
        try {
            // 查询知识点表中重复的title
            String sql = "SELECT title, COUNT(*) as count FROM knowledge_points GROUP BY title HAVING COUNT(*) > 1";
            List<Map<String, Object>> duplicateTitles = jdbcTemplate.queryForList(sql);

            if (duplicateTitles.isEmpty()) {
                System.out.println("✅ 知识点表中没有重复的title数据");
            } else {
                System.out.println("❌ 知识点表中发现" + duplicateTitles.size() + "个重复的title:");
                for (Map<String, Object> row : duplicateTitles) {
                    System.out.println("   - '" + row.get("title") + "': 重复" + row.get("count") + "次");
                }
            }

            // 查询知识点总数
            String countSql = "SELECT COUNT(*) FROM knowledge_points";
            int totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
            System.out.println("知识点表总记录数: " + totalCount);

            // 查询不同的courseId数量
            String courseCountSql = "SELECT COUNT(DISTINCT course_id) FROM knowledge_points";
            int courseCount = jdbcTemplate.queryForObject(courseCountSql, Integer.class);
            System.out.println("知识点表中不同的课程ID数量: " + courseCount);

            // 查询每个courseId的知识点数量
            String courseDistributionSql = "SELECT course_id, COUNT(*) as count FROM knowledge_points GROUP BY course_id";
            List<Map<String, Object>> courseDistribution = jdbcTemplate.queryForList(courseDistributionSql);
            System.out.println("各课程的知识点分布:");
            for (Map<String, Object> row : courseDistribution) {
                System.out.println("   - 课程ID " + row.get("course_id") + ": " + row.get("count") + "个知识点");
            }
        } catch (Exception e) {
            System.err.println("验证知识点数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 验证courses表是否已生成数据
     */
    private void verifyCoursesDataGenerated() {
        try {
            // 查询courses表中的所有数据
            String sql = "SELECT * FROM courses";
            List<Map<String, Object>> courses = jdbcTemplate.queryForList(sql);

            if (courses.isEmpty()) {
                System.out.println("❌ courses表中没有数据");
            } else {
                System.out.println("✅ courses表中已生成" + courses.size() + "条课程数据");
                System.out.println("课程列表:");
                for (Map<String, Object> course : courses) {
                    System.out.println("   - ID: " + course.get("course_id") + ", 标题: " + course.get("title") + ", 描述: " + course.get("description"));
                }
            }

            // 验证courses表中的course_id是否与knowledge_points表中的course_id匹配
            String sqlCheckMatching = "SELECT DISTINCT k.course_id FROM knowledge_points k LEFT JOIN courses c ON k.course_id = c.course_id WHERE c.course_id IS NULL";
            List<Map<String, Object>> unmatchedCourseIds = jdbcTemplate.queryForList(sqlCheckMatching);

            if (unmatchedCourseIds.isEmpty()) {
                System.out.println("✅ 所有knowledge_points表中的course_id在courses表中都有对应记录");
            } else {
                System.out.println("❌ 发现" + unmatchedCourseIds.size() + "个course_id在courses表中没有对应记录:");
                for (Map<String, Object> row : unmatchedCourseIds) {
                    System.out.println("   - 课程ID: " + row.get("course_id"));
                }
            }
        } catch (Exception e) {
            System.err.println("验证课程数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}