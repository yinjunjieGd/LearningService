package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.KnowledgePointsEntity;
import com.gaodun.learningservice.manager.KnowledgePointsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识点Controller
 * @author shkstart
 * @create 2025-10-23 2:38
 */
@RestController
@RequestMapping("/api/knowledge-points")
@RequiredArgsConstructor
@Slf4j
public class KnowledgePointsController {
    @Autowired
    private KnowledgePointsManager knowledgePointsManager;
    
    // 根据ID查询知识点
    @GetMapping("/getById")
    public KnowledgePointsEntity getById(@RequestParam Integer pointId) {
        log.info("Received request to get knowledge point by id: {}", pointId);
        return knowledgePointsManager.selectById(pointId);
    }
    
    // 查询所有知识点
    @GetMapping("/list")
    public List<KnowledgePointsEntity> list() {
        log.info("Received request to list all knowledge points");
        return knowledgePointsManager.selectAll();
    }
    
    // 根据课程ID查询知识点
    @GetMapping("/listByCourseId")
    public List<KnowledgePointsEntity> listByCourseId(@RequestParam Integer courseId) {
        log.info("Received request to list knowledge points by course id: {}", courseId);
        return knowledgePointsManager.selectByCourseId(courseId);
    }
    
    // 插入知识点
    @PostMapping("/insert")
    public int insert(@RequestBody KnowledgePointsEntity knowledgePoint) {
        log.info("Received request to insert knowledge point: {}", knowledgePoint);
        return knowledgePointsManager.insert(knowledgePoint);
    }
    
    // 更新知识点
    @PostMapping("/update")
    public int update(@RequestBody KnowledgePointsEntity knowledgePoint) {
        log.info("Received request to update knowledge point: {}", knowledgePoint);
        return knowledgePointsManager.update(knowledgePoint);
    }
    
    // 删除知识点
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer pointId) {
        log.info("Received request to delete knowledge point by id: {}", pointId);
        return knowledgePointsManager.delete(pointId);
    }
}