package com.gaodun.learningservice.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class CheckAllTablesStructureTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void checkAllTablesStructure() {
        // 要检查的表列表
        String[] tables = {"courses", "questions", "knowledge_points", "user_learning_profiles", "user_learning_records", "users"};
        
        for (String table : tables) {
            System.out.println("\n===== 检查" + table + "表结构 =====");
            
            // 查询表结构
            String queryTableStructureSql = "SHOW COLUMNS FROM " + table;
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(queryTableStructureSql);
            
            System.out.println(table + "表的列信息：");
            for (Map<String, Object> column : columns) {
                System.out.println(column.get("Field") + " (" + column.get("Type") + ") - " + column.get("Null") + " - " + column.get("Key") + " - " + column.get("Default"));
            }
            
            // 查询表的前5条数据
            System.out.println("\n" + table + "表的前5条数据：");
            String queryDataSql = "SELECT * FROM " + table + " LIMIT 5";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(queryDataSql);
            
            if (data.isEmpty()) {
                System.out.println("表中暂无数据");
            } else {
                for (Map<String, Object> row : data) {
                    System.out.println(row);
                }
            }
        }
    }
}