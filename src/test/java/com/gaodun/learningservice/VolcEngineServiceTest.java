package com.gaodun.learningservice;

import com.gaodun.learningservice.config.VolcEngineConfig;
import com.gaodun.learningservice.DTO.VolcEngineTextRequest;
import com.gaodun.learningservice.DTO.VolcEngineResponse;
import com.gaodun.learningservice.manager.VolcEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 火山引擎API服务测试类
 * 
 * 测试前请确保:
 * 1. application.properties中已配置真实的API Key
 * 2. volcengine.model.text已设置为有效的Model ID或Endpoint ID
 * 3. 网络连接正常
 */
@SpringBootTest
public class VolcEngineServiceTest {

    @Autowired
    private VolcEngineService volcEngineService;

    @Autowired
    private VolcEngineConfig volcEngineConfig;

    @BeforeEach
    public void setUp() {
        // 检查配置是否已设置
        assertNotNull(volcEngineConfig, "VolcEngineConfig未正确注入");
        assertNotNull(volcEngineService, "VolcEngineService未正确注入");
        
        String apiKey = volcEngineConfig.getApiKey();
        String modelId = volcEngineConfig.getTextModelId();
        
        System.out.println("========== 配置检查 ==========");
        System.out.println("API Base URL: " + volcEngineConfig.getBaseUrl());
        System.out.println("API Key配置状态: " + (apiKey != null && !apiKey.equals("YOUR_API_KEY_HERE") ? "已配置" : "未配置(使用占位符)"));
        System.out.println("Text Model ID: " + modelId);
        System.out.println("==============================\n");
        
        if (apiKey == null || apiKey.equals("YOUR_API_KEY_HERE")) {
            System.err.println("警告: API Key未配置,测试将失败!");
            System.err.println("请在application.properties中设置真实的volcengine.api.key");
        }
        
        if (modelId == null || modelId.equals("YOUR_TEXT_MODEL_ID")) {
            System.err.println("警告: Model ID未配置,测试将失败!");
            System.err.println("请在application.properties中设置真实的volcengine.model.text");
        }
    }

    /**
     * 测试简单的文本推理调用
     */
    @Test
    public void testSimpleTextInference() {
        System.out.println("\n========== 测试1: 简单文本推理 ==========");
        
        // 构建请求
        VolcEngineTextRequest request = new VolcEngineTextRequest();
        request.setModel(volcEngineConfig.getTextModelId());
        
        List<VolcEngineTextRequest.Message> messages = new ArrayList<>();
        VolcEngineTextRequest.Message message = new VolcEngineTextRequest.Message();
        message.setRole("user");
        message.setContent("你好,请用一句话介绍一下自己。");
        messages.add(message);
        
        request.setMessages(messages);
        request.setTemperature(0.7);
        request.setMaxTokens(100);
        
        System.out.println("发送请求: " + message.getContent());
        
        try {
            // 调用API
            VolcEngineResponse response = volcEngineService.textInference(request);
            
            // 验证响应
            assertNotNull(response, "响应不应为null");
            assertNotNull(response.getChoices(), "choices不应为null");
            assertFalse(response.getChoices().isEmpty(), "choices不应为空");
            
            String aiResult = response.getChoices().get(0).getMessage().getContent();
            assertNotNull(aiResult, "AI返回内容不应为null");
            assertFalse(aiResult.trim().isEmpty(), "AI返回内容不应为空");
            
            System.out.println("\n✓ 测试通过!");
            System.out.println("AI响应: " + aiResult);
            System.out.println("==============================\n");
            
        } catch (Exception e) {
            System.err.println("\n✗ 测试失败!");
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("==============================\n");
            fail("API调用失败: " + e.getMessage());
        }
    }

    /**
     * 测试知识点掌握度分析场景(模拟实际业务)
     */
    @Test
    public void testKnowledgeMasteryAnalysis() {
        System.out.println("\n========== 测试2: 知识点掌握度分析 ==========");
        
        // 构建请求
        VolcEngineTextRequest request = new VolcEngineTextRequest();
        request.setModel(volcEngineConfig.getTextModelId());
        
        List<VolcEngineTextRequest.Message> messages = new ArrayList<>();
        
        // 系统提示
        VolcEngineTextRequest.Message systemMessage = new VolcEngineTextRequest.Message();
        systemMessage.setRole("system");
        systemMessage.setContent("你是一位专业的会计学习分析专家。请根据学生的答题记录分析其对知识点的掌握程度。");
        messages.add(systemMessage);
        
        // 用户问题
        VolcEngineTextRequest.Message userMessage = new VolcEngineTextRequest.Message();
        userMessage.setRole("user");
        userMessage.setContent(
            "知识点: 借贷记账法\n" +
            "知识点描述: 借贷记账法是会计核算的基本方法,遵循'有借必有贷,借贷必相等'的原则\n" +
            "答题记录:\n" +
            "- 题目1: 正确 (首次作答)\n" +
            "- 题目2: 错误 -> 正确 (第二次作答)\n" +
            "- 题目3: 正确 (首次作答)\n" +
            "\n请分析该学生对'借贷记账法'的掌握程度,并给出0-100的掌握度评分和简短分析(50字以内)。\n" +
            "请严格按照以下JSON格式返回:\n" +
            "{\"score\": 85, \"analysis\": \"学生对借贷记账法基本掌握,但在复杂场景应用上还需加强练习\"}"
        );
        messages.add(userMessage);
        
        request.setMessages(messages);
        request.setTemperature(0.3); // 降低随机性,提高稳定性
        request.setMaxTokens(200);
        
        System.out.println("分析场景: 借贷记账法知识点掌握度");
        
        try {
            // 调用API
            VolcEngineResponse response = volcEngineService.textInference(request);
            
            // 验证响应
            assertNotNull(response, "响应不应为null");
            assertNotNull(response.getChoices(), "choices不应为null");
            assertFalse(response.getChoices().isEmpty(), "choices不应为空");
            
            String aiResult = response.getChoices().get(0).getMessage().getContent().trim();
            assertNotNull(aiResult, "AI返回内容不应为null");
            assertFalse(aiResult.isEmpty(), "AI返回内容不应为空");
            
            System.out.println("\n✓ 测试通过!");
            System.out.println("AI分析结果: " + aiResult);
            
            // 尝试验证JSON格式
            if (aiResult.contains("{\"score\"") || aiResult.contains("\"score\":")) {
                System.out.println("✓ 返回结果包含预期的JSON格式");
            } else {
                System.out.println("⚠ 返回结果格式可能需要调整");
            }
            
            System.out.println("==============================\n");
            
        } catch (Exception e) {
            System.err.println("\n✗ 测试失败!");
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("==============================\n");
            fail("API调用失败: " + e.getMessage());
        }
    }

    /**
     * 测试多轮对话
     */
    @Test
    public void testMultiTurnConversation() {
        System.out.println("\n========== 测试3: 多轮对话 ==========");
        
        // 构建请求
        VolcEngineTextRequest request = new VolcEngineTextRequest();
        request.setModel(volcEngineConfig.getTextModelId());
        
        List<VolcEngineTextRequest.Message> messages = new ArrayList<>();
        
        // 第一轮对话
        VolcEngineTextRequest.Message message1 = new VolcEngineTextRequest.Message();
        message1.setRole("user");
        message1.setContent("什么是资产负债表?");
        messages.add(message1);
        
        VolcEngineTextRequest.Message response1 = new VolcEngineTextRequest.Message();
        response1.setRole("assistant");
        response1.setContent("资产负债表是反映企业在某一特定日期财务状况的会计报表。");
        messages.add(response1);
        
        // 第二轮对话
        VolcEngineTextRequest.Message message2 = new VolcEngineTextRequest.Message();
        message2.setRole("user");
        message2.setContent("它包含哪些主要部分?请简要说明。");
        messages.add(message2);
        
        request.setMessages(messages);
        request.setTemperature(0.7);
        request.setMaxTokens(200);
        
        System.out.println("第一轮: " + message1.getContent());
        System.out.println("第二轮: " + message2.getContent());
        
        try {
            // 调用API
            VolcEngineResponse response = volcEngineService.textInference(request);
            
            // 验证响应
            assertNotNull(response, "响应不应为null");
            String aiResult = response.getChoices().get(0).getMessage().getContent();
            assertNotNull(aiResult, "AI返回内容不应为null");
            
            System.out.println("\n✓ 测试通过!");
            System.out.println("AI响应: " + aiResult);
            System.out.println("==============================\n");
            
        } catch (Exception e) {
            System.err.println("\n✗ 测试失败!");
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("==============================\n");
            fail("API调用失败: " + e.getMessage());
        }
    }
}
