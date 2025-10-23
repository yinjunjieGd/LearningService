package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.DTO.*;
import com.gaodun.learningservice.manager.VolcEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 火山引擎大模型控制器
 * 提供文本推理、视觉理解、图片生成三种API接口
 */
@RestController
@RequestMapping("/api/volcengine")
public class VolcEngineController {
    
    @Autowired
    private VolcEngineService volcEngineService;
    
    /**
     * 文本推理接口
     * 用于调用火山引擎的文本生成模型
     * 
     * @param request 包含messages、temperature等参数
     * @return 模型生成的文本响应
     */
    @PostMapping("/text/inference")
    public ResponseEntity<VolcEngineResponse> textInference(@RequestBody VolcEngineTextRequest request) {
        try {
            VolcEngineResponse response = volcEngineService.textInference(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 视觉理解接口
     * 用于调用火山引擎的多模态模型，支持图片+文本输入
     * 
     * @param request 包含图片URL和文本描述的messages
     * @return 模型对图片的理解和分析结果
     */
    @PostMapping("/vision/understanding")
    public ResponseEntity<VolcEngineResponse> visionUnderstanding(@RequestBody VolcEngineVisionRequest request) {
        try {
            VolcEngineResponse response = volcEngineService.visionUnderstanding(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 图片生成接口
     * 用于调用火山引擎的文生图模型
     * 
     * @param request 包含prompt、size等参数
     * @return 生成的图片URL或base64编码
     */
    @PostMapping("/image/generation")
    public ResponseEntity<VolcEngineResponse> imageGeneration(@RequestBody VolcEngineImageRequest request) {
        try {
            VolcEngineResponse response = volcEngineService.imageGeneration(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
