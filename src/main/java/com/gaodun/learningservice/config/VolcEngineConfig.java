package com.gaodun.learningservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 火山引擎大模型配置类
 * 用于管理火山引擎API的配置信息
 */
@Configuration
public class VolcEngineConfig {
    
    @Value("${volcengine.api.key:}")
    private String apiKey;
    
    @Value("${volcengine.api.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String baseUrl;
    
    @Value("${volcengine.model.text:ep-20241226105107-xxxxxx}")
    private String textModelId;
    
    @Value("${volcengine.model.vision:ep-20241226105107-xxxxxx}")
    private String visionModelId;
    
    @Value("${volcengine.model.image:ep-20241226105107-xxxxxx}")
    private String imageModelId;
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public String getTextModelId() {
        return textModelId;
    }
    
    public String getVisionModelId() {
        return visionModelId;
    }
    
    public String getImageModelId() {
        return imageModelId;
    }
}
