package com.gaodun.learningservice.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaodun.learningservice.DTO.*;
import com.gaodun.learningservice.config.VolcEngineConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 火山引擎大模型服务类
 * 封装文本推理、视觉理解、图片生成三种API调用
 */
@Service
public class VolcEngineService {
    
    @Autowired
    private VolcEngineConfig config;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 文本推理API调用
     */
    public VolcEngineResponse textInference(VolcEngineTextRequest request) {
        try {
            String url = config.getBaseUrl() + "/chat/completions";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", config.getTextModelId());
            requestBody.put("messages", request.getMessages());
            
            if (request.getTemperature() != null) {
                requestBody.put("temperature", request.getTemperature());
            }
            if (request.getMaxTokens() != null) {
                requestBody.put("max_tokens", request.getMaxTokens());
            }
            if (request.getTopP() != null) {
                requestBody.put("top_p", request.getTopP());
            }
            
            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
            );
            
            return objectMapper.readValue(response.getBody(), VolcEngineResponse.class);
            
        } catch (Exception e) {
            throw new RuntimeException("文本推理API调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 视觉理解API调用
     */
    public VolcEngineResponse visionUnderstanding(VolcEngineVisionRequest request) {
        try {
            String url = config.getBaseUrl() + "/chat/completions";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", config.getVisionModelId());
            requestBody.put("messages", request.getMessages());
            
            if (request.getTemperature() != null) {
                requestBody.put("temperature", request.getTemperature());
            }
            if (request.getMaxTokens() != null) {
                requestBody.put("max_tokens", request.getMaxTokens());
            }
            
            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
            );
            
            return objectMapper.readValue(response.getBody(), VolcEngineResponse.class);
            
        } catch (Exception e) {
            throw new RuntimeException("视觉理解API调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 图片生成API调用
     */
    public VolcEngineResponse imageGeneration(VolcEngineImageRequest request) {
        try {
            String url = config.getBaseUrl() + "/images/generations";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", config.getImageModelId());
            requestBody.put("prompt", request.getPrompt());
            
            if (request.getSize() != null) {
                requestBody.put("size", request.getSize());
            }
            if (request.getResponseFormat() != null) {
                requestBody.put("response_format", request.getResponseFormat());
            }
            if (request.getN() != null) {
                requestBody.put("n", request.getN());
            }
            
            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
            );
            
            return objectMapper.readValue(response.getBody(), VolcEngineResponse.class);
            
        } catch (Exception e) {
            throw new RuntimeException("图片生成API调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建HTTP请求头
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + config.getApiKey());
        return headers;
    }
}
