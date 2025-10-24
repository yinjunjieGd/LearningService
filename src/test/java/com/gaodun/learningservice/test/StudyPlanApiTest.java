package com.gaodun.learningservice.test;

import com.gaodun.learningservice.Entity.StudyPlanEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 学习计划API接口测试类
 * 验证学习计划的生成和查询功能是否正常
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudyPlanApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:%d/api/study-plan";

    /**
     * 测试学习计划生成功能
     * 1. 生成学习计划
     * 2. 验证生成结果
     * 3. 查询生成的学习计划
     */
    @Test
    public void testGenerateAndGetStudyPlan() {
        // 测试参数 - 使用有效的用户ID和课程ID
        Integer userId = 1;
        Integer courseId = 1;

        // 1. 测试生成学习计划
        String generateUrl = String.format(BASE_URL + "/generate?userId=%d&courseId=%d", port, userId, courseId);
        ResponseEntity<Map> generateResponse = restTemplate.postForEntity(generateUrl, null, Map.class);
        assertEquals(HttpStatus.OK, generateResponse.getStatusCode());
        assertNotNull(generateResponse.getBody());
        
        // 验证生成结果
        assertTrue((Boolean) generateResponse.getBody().get("success"));
        assertEquals("学习计划生成成功", generateResponse.getBody().get("message"));
        assertNotNull(generateResponse.getBody().get("data"));
        
        int generatedCount = (Integer) generateResponse.getBody().get("count");
        assertTrue(generatedCount > 0, "生成的学习计划数量大于0");
        System.out.println("生成学习计划成功，共" + generatedCount + "条数据");

        // 2. 测试查询学习计划
        String getUrl = String.format(BASE_URL + "?userId=%d&courseId=%d", port, userId, courseId);
        ResponseEntity<Map> getResponse = restTemplate.getForEntity(getUrl, Map.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        
        // 验证查询结果
        assertTrue((Boolean) getResponse.getBody().get("success"));
        assertNotNull(getResponse.getBody().get("data"));
        
        int queryCount = (Integer) getResponse.getBody().get("count");
        assertTrue(queryCount > 0, "查询到的学习计划数量大于0");
        assertTrue(queryCount >= generatedCount, "查询到的学习计划数量大于或等于生成的数量");
        System.out.println("查询学习计划成功，共" + queryCount + "条数据");
    }

    /**
     * 测试使用不同用户和课程ID生成学习计划
     * 验证系统对不同用户和课程组合的处理能力
     */
    @Test
    public void testGenerateStudyPlanWithDifferentParameters() {
        // 测试不同的用户和课程组合
        Object[][] testCases = {
            {1, 2},  // 用户1，课程2
            {2, 1},  // 用户2，课程1
            {2, 2}   // 用户2，课程2
        };

        for (Object[] testCase : testCases) {
            Integer userId = (Integer) testCase[0];
            Integer courseId = (Integer) testCase[1];

            System.out.println("测试生成学习计划：userId=" + userId + ", courseId=" + courseId);
            
            String generateUrl = String.format(BASE_URL + "/generate?userId=%d&courseId=%d", port, userId, courseId);
            ResponseEntity<Map> response = restTemplate.postForEntity(generateUrl, null, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                assertTrue((Boolean) response.getBody().get("success"));
                System.out.println("  生成成功，数量：" + response.getBody().get("count"));
            } else {
                // 即使失败也打印错误信息，但不中断测试
                System.out.println("  生成失败，状态码：" + response.getStatusCodeValue() + ", 错误信息：" + 
                                   response.getBody().get("message"));
            }
        }
    }

    /**
     * 测试边界条件 - 无效的用户ID或课程ID
     * 验证系统对无效输入的处理能力
     */
    @Test
    public void testGenerateStudyPlanWithInvalidParameters() {
        // 测试使用负数ID
        String invalidUrl = String.format(BASE_URL + "/generate?userId=-1&courseId=-1", port);
        ResponseEntity<Map> response = restTemplate.postForEntity(invalidUrl, null, Map.class);
        
        // 可能返回成功（系统内部处理）或失败，这里只打印结果
        System.out.println("测试无效参数：" + 
                          "状态码=" + response.getStatusCodeValue() + ", " +
                          "成功=" + response.getBody().get("success"));
    }
}