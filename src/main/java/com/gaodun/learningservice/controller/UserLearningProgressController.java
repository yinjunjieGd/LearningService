package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.UserLearningProgressEntity;
import com.gaodun.learningservice.manager.UserLearningProgressManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户课程学习进度Controller
 * @author shkstart
 */
@RestController
@RequestMapping("/api/user-learning-progress")
@RequiredArgsConstructor
@Slf4j
public class UserLearningProgressController {
    
    @Autowired
    private UserLearningProgressManager userLearningProgressManager;
    
    /**
     * 根据ID查询学习进度
     */
    @GetMapping("/getById")
    public UserLearningProgressEntity getById(@RequestParam Long id) {
        log.info("Received request to get user learning progress by id: {}", id);
        return userLearningProgressManager.selectById(id);
    }
    
    /**
     * 根据用户ID查询学习进度列表
     */
    @GetMapping("/listByUserId")
    public List<UserLearningProgressEntity> listByUserId(@RequestParam Long userId) {
        log.info("Received request to list user learning progress by user id: {}", userId);
        return userLearningProgressManager.selectByUserId(userId);
    }
    
    /**
     * 根据课程ID查询学习进度列表
     */
    @GetMapping("/listByCourseId")
    public List<UserLearningProgressEntity> listByCourseId(@RequestParam Long courseId) {
        log.info("Received request to list user learning progress by course id: {}", courseId);
        return userLearningProgressManager.selectByCourseId(courseId);
    }
    
    /**
     * 根据用户ID和课程ID查询学习进度
     */
    @GetMapping("/getByUserIdAndCourseId")
    public UserLearningProgressEntity getByUserIdAndCourseId(@RequestParam Long userId, @RequestParam Long courseId) {
        log.info("Received request to get user learning progress by user id and course id: userId={}, courseId={}", userId, courseId);
        return userLearningProgressManager.selectByUserIdAndCourseId(userId, courseId);
    }
    
    /**
     * 查询所有学习进度
     */
    @GetMapping("/list")
    public List<UserLearningProgressEntity> list() {
        log.info("Received request to list all user learning progress");
        return userLearningProgressManager.selectAll();
    }
    
    /**
     * 插入学习进度
     */
    @PostMapping("/insert")
    public int insert(@RequestBody UserLearningProgressEntity progress) {
        log.info("Received request to insert user learning progress: {}", progress);
        return userLearningProgressManager.insert(progress);
    }
    
    /**
     * 更新学习进度
     */
    @PutMapping("/update")
    public int update(@RequestBody UserLearningProgressEntity progress) {
        log.info("Received request to update user learning progress: {}", progress);
        return userLearningProgressManager.update(progress);
    }
    
    /**
     * 删除学习进度
     */
    @DeleteMapping("/delete")
    public int delete(@RequestParam Long id) {
        log.info("Received request to delete user learning progress by id: {}", id);
        return userLearningProgressManager.delete(id);
    }
    
    /**
     * 根据用户ID删除学习进度
     */
    @DeleteMapping("/deleteByUserId")
    public int deleteByUserId(@RequestParam Long userId) {
        log.info("Received request to delete user learning progress by user id: {}", userId);
        return userLearningProgressManager.deleteByUserId(userId);
    }
}
