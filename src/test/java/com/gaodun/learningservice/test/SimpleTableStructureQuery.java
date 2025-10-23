package com.gaodun.learningservice.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class SimpleTableStructureQuery {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void queryAllTablesStructure() {
        try {
            // 逐个查询表结构，确保输出完整
            queryTableStructure("courses");
            queryTableStructure("questions");
            queryTableStructure("knowledge_points");
            queryTableStructure("user_learning_profiles");
            queryTableStructure("user_learning_records");
            queryTableStructure("users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryTableStructure(String tableName) {
        System.out.println("\n----- " + tableName + "表结构 -----");
        String sql = "SHOW CREATE TABLE " + tableName;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        if (!result.isEmpty()) {
            Map<String, Object> row = result.get(0);
            System.out.println(row.get("Create Table"));
        }
    }
}