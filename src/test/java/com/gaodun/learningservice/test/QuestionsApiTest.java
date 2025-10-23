package com.gaodun.learningservice.test;

import com.gaodun.learningservice.DTO.QuestionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 题目API测试类
 * 用于测试getQuestionsByCourseId接口
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionsApiTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private static final String BASE_URL = "http://localhost:%d/api/questions";
    
    @Test
    public void testGetQuestionsByCourseId() {
        // 1. 测试用例：使用存在的课程ID（例如课程ID为4，初级会计实务）
        Integer courseId = 4; // 初级会计实务课程ID
        String url = String.format(BASE_URL + "/getQuestionsByCourseId?courseId=%d", port, courseId);
        
        // 发送GET请求
        ResponseEntity<List<QuestionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QuestionDTO>>() {}
        );
        
        // 验证响应状态码
        assertEquals(200, response.getStatusCodeValue(), "HTTP响应状态码应为200");
        
        // 获取响应体
        List<QuestionDTO> questions = response.getBody();
        assertNotNull(questions, "响应体不应为空");
        
        // 验证返回题目数量不超过20
        assertTrue(questions.size() <= 20, "返回题目数量不应超过20道");
        
        // 如果有题目返回，验证每个题目的字段是否正确
        if (!questions.isEmpty()) {
            System.out.println("成功获取到" + questions.size() + "道题目");
            // 打印前3道题目的信息作为示例
            int displayCount = Math.min(3, questions.size());
            for (int i = 0; i < displayCount; i++) {
                QuestionDTO question = questions.get(i);
                System.out.println("题目ID: " + question.getQuestionId());
                System.out.println("题目内容: " + question.getStem());
                System.out.println("题目类型: " + question.getType());
                System.out.println("选项: " + question.getOptions());
                System.out.println("正确答案: " + question.getCorrectAnswer());
                System.out.println("------------------------");
                
                // 验证每个题目是否包含所需的字段
                assertNotNull(question.getQuestionId(), "题目ID不应为空");
                assertNotNull(question.getStem(), "题目内容不应为空");
                assertNotNull(question.getType(), "题目类型不应为空");
                assertNotNull(question.getOptions(), "题目选项不应为空");
                assertNotNull(question.getCorrectAnswer(), "正确答案不应为空");
                assertTrue(question.getOptions() instanceof Map, "选项应为Map类型");
            }
            
            // 验证随机性：多次调用接口，检查返回的题目是否不同
            System.out.println("\n测试随机性：再次调用接口...");
            ResponseEntity<List<QuestionDTO>> response2 = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<QuestionDTO>>() {}
            );
            List<QuestionDTO> questions2 = response2.getBody();
            if (questions2 != null && !questions2.isEmpty() && questions.size() > 1) {
                // 比较两次返回的第一道题目ID是否不同（大概率不同）
                System.out.println("第一次调用首题ID: " + questions.get(0).getQuestionId());
                System.out.println("第二次调用首题ID: " + questions2.get(0).getQuestionId());
                System.out.println("随机性测试完成（如果两次ID不同则说明实现了随机选择）");
            }
        } else {
            System.out.println("未获取到题目数据，可能数据库中没有该课程的题目");
        }
        
        // 2. 测试用例：使用不存在的课程ID
        Integer nonExistentCourseId = 999; // 不存在的课程ID
        String nonExistentUrl = String.format(BASE_URL + "/getQuestionsByCourseId?courseId=%d", port, nonExistentCourseId);
        
        ResponseEntity<List<QuestionDTO>> nonExistentResponse = restTemplate.exchange(
                nonExistentUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QuestionDTO>>() {}
        );
        
        // 验证响应状态码
        assertEquals(200, nonExistentResponse.getStatusCodeValue(), "HTTP响应状态码应为200");
        
        // 验证返回空列表
        List<QuestionDTO> nonExistentQuestions = nonExistentResponse.getBody();
        assertNotNull(nonExistentQuestions, "响应体不应为空");
        assertTrue(nonExistentQuestions.isEmpty(), "对于不存在的课程ID，应返回空列表");
        
        System.out.println("对不存在的课程ID，返回了空列表，符合预期");
        System.out.println("QuestionsApiTest测试成功完成！");
    }
}