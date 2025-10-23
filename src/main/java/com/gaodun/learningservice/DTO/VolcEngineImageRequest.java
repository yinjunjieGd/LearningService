package com.gaodun.learningservice.DTO;

/**
 * 火山引擎图片生成请求DTO
 */
public class VolcEngineImageRequest {
    
    private String prompt;
    private String size; // "1024x1024", "512x512" etc.
    private String responseFormat; // "url" or "b64_json"
    private Integer n; // 生成图片数量
    
    public String getPrompt() {
        return prompt;
    }
    
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getResponseFormat() {
        return responseFormat;
    }
    
    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }
    
    public Integer getN() {
        return n;
    }
    
    public void setN(Integer n) {
        this.n = n;
    }
}
