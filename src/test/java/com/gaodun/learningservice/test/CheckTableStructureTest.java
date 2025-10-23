package com.gaodun.learningservice.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class CheckTableStructureTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void checkCoursesTableStructure() {
        System.out.println("\n===== 检查courses表结构 =====");
        
        // 查询courses表的结构
        String queryTableStructureSql = "SHOW COLUMNS FROM courses";
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(queryTableStructureSql);
        
        System.out.println("courses表的列信息：");
        for (Map<String, Object> column : columns) {
            System.out.println(column.get("Field") + " (" + column.get("Type") + ") - " + column.get("Null"));
        }
        
        // 同时检查knowledge_points表的结构
        System.out.println("\n===== 检查knowledge_points表结构 =====");
        String queryKnowledgePointsStructureSql = "SHOW COLUMNS FROM knowledge_points";
        List<Map<String, Object>> kpColumns = jdbcTemplate.queryForList(queryKnowledgePointsStructureSql);
        
        System.out.println("knowledge_points表的列信息：");
        for (Map<String, Object> column : kpColumns) {
            System.out.println(column.get("Field") + " (" + column.get("Type") + ") - " + column.get("Null"));
        }
        
        // 查询当前课程数据
        System.out.println("\n===== 查询当前courses表数据 =====");
        String queryCoursesDataSql = "SELECT * FROM courses LIMIT 10";
        List<Map<String, Object>> coursesData = jdbcTemplate.queryForList(queryCoursesDataSql);
        
        if (coursesData.isEmpty()) {
            System.out.println("courses表当前没有数据");
        } else {
            System.out.println("courses表数据：");
            for (Map<String, Object> row : coursesData) {
                System.out.println(row);
            }
        }
    }
}