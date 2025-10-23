package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;
import com.gaodun.learningservice.manager.UserLearningProfilesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户学习档案Controller
 * @author shkstart
 */
@RestController
@RequestMapping("/api/user-learning-profiles")
@RequiredArgsConstructor
@Slf4j
public class UserLearningProfilesController {
    @Autowired
    private UserLearningProfilesManager userLearningProfilesManager;
    
    // 根据ID查询用户学习档案
    @GetMapping("/getById")
    public UserLearningProfilesEntity getById(@RequestParam Integer profileId) {
        log.info("Received request to get user learning profile by id: {}", profileId);
        return userLearningProfilesManager.selectById(profileId);
    }
    
    // 根据用户ID查询学习档案
    @GetMapping("/listByUserId")
    public List<UserLearningProfilesEntity> listByUserId(@RequestParam Integer userId) {
        log.info("Received request to list user learning profiles by user id: {}", userId);
        return userLearningProfilesManager.selectByUserId(userId);
    }
    
    // 根据用户ID和课程ID查询学习档案
    @GetMapping("/getByUserIdAndCourseId")
    public UserLearningProfilesEntity getByUserIdAndCourseId(@RequestParam Integer userId, @RequestParam Integer courseId) {
        log.info("Received request to get user learning profile by user id and course id: userId={}, courseId={}", userId, courseId);
        return userLearningProfilesManager.selectByUserIdAndCourseId(userId, courseId);
    }
    
    // 查询所有学习档案
    @GetMapping("/list")
    public List<UserLearningProfilesEntity> list() {
        log.info("Received request to list all user learning profiles");
        return userLearningProfilesManager.selectAll();
    }
    
    // 插入学习档案
    @PostMapping("/insert")
    public int insert(@RequestBody UserLearningProfilesEntity profile) {
        log.info("Received request to insert user learning profile: {}", profile);
        return userLearningProfilesManager.insert(profile);
    }
    
    // 更新学习档案
    @PutMapping("/update")
    public int update(@RequestBody UserLearningProfilesEntity profile) {
        log.info("Received request to update user learning profile: {}", profile);
        return userLearningProfilesManager.update(profile);
    }
    
    // 删除学习档案
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer profileId) {
        log.info("Received request to delete user learning profile by id: {}", profileId);
        return userLearningProfilesManager.delete(profileId);
    }
}