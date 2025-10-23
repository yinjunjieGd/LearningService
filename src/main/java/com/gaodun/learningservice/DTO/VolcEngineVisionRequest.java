package com.gaodun.learningservice.DTO;

import java.util.List;

/**
 * 火山引擎视觉理解请求DTO
 */
public class VolcEngineVisionRequest {
    
    private List<Message> messages;
    private Double temperature;
    private Integer maxTokens;
    
    public static class Message {
        private String role;
        private Object content; // 可以是String或List<ContentItem>
        
        public Message() {}
        
        public Message(String role, Object content) {
            this.role = role;
            this.content = content;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public Object getContent() {
            return content;
        }
        
        public void setContent(Object content) {
            this.content = content;
        }
    }
    
    public static class ContentItem {
        private String type; // "text" or "image_url"
        private String text;
        private ImageUrl imageUrl;
        
        public ContentItem() {}
        
        public ContentItem(String type) {
            this.type = type;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
        
        public ImageUrl getImageUrl() {
            return imageUrl;
        }
        
        public void setImageUrl(ImageUrl imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
    
    public static class ImageUrl {
        private String url;
        
        public ImageUrl() {}
        
        public ImageUrl(String url) {
            this.url = url;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Integer getMaxTokens() {
        return maxTokens;
    }
    
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }
}
