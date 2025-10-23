package com.gaodun.learningservice.controller;

import com.gaodun.learningservice.Entity.UsersEntity;
import com.gaodun.learningservice.manager.UsersManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户Controller
 * @author shkstart
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {
    @Autowired
    private UsersManager usersManager;
    
    // 根据ID查询用户
    @GetMapping("/getById")
    public UsersEntity getById(@RequestParam Integer userId) {
        log.info("Received request to get user by id: {}", userId);
        return usersManager.selectById(userId);
    }
    
    // 根据用户名查询用户
    @GetMapping("/getByUsername")
    public UsersEntity getByUsername(@RequestParam String username) {
        log.info("Received request to get user by username: {}", username);
        return usersManager.selectByUsername(username);
    }
    
    // 根据邮箱查询用户
    @GetMapping("/getByEmail")
    public UsersEntity getByEmail(@RequestParam String email) {
        log.info("Received request to get user by email: {}", email);
        return usersManager.selectByEmail(email);
    }
    
    // 根据角色查询用户
    @GetMapping("/listByRole")
    public List<UsersEntity> listByRole(@RequestParam String role) {
        log.info("Received request to list users by role: {}", role);
        return usersManager.selectByRole(role);
    }
    
    // 查询所有用户
    @GetMapping("/list")
    public List<UsersEntity> list() {
        log.info("Received request to list all users");
        return usersManager.selectAll();
    }
    
    // 插入用户
    @PostMapping("/insert")
    public int insert(@RequestBody UsersEntity user) {
        log.info("Received request to insert user: {}", user);
        return usersManager.insert(user);
    }
    
    // 更新用户
    @PutMapping("/update")
    public int update(@RequestBody UsersEntity user) {
        log.info("Received request to update user: {}", user);
        return usersManager.update(user);
    }
    
    // 删除用户
    @DeleteMapping("/delete")
    public int delete(@RequestParam Integer userId) {
        log.info("Received request to delete user by id: {}", userId);
        return usersManager.delete(userId);
    }
}