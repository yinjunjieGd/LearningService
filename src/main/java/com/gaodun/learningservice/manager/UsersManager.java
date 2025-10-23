package com.gaodun.learningservice.manager;

import com.gaodun.learningservice.Entity.UsersEntity;

import java.util.List;

/**
 * 用户Manager接口
 * @author shkstart
 */
public interface UsersManager {
    // 根据ID查询用户
    UsersEntity selectById(Integer userId);
    
    // 根据用户名查询用户
    UsersEntity selectByUsername(String username);
    
    // 根据邮箱查询用户
    UsersEntity selectByEmail(String email);
    
    // 根据角色查询用户
    List<UsersEntity> selectByRole(String role);
    
    // 查询所有用户
    List<UsersEntity> selectAll();
    
    // 插入用户
    int insert(UsersEntity user);
    
    // 更新用户
    int update(UsersEntity user);
    
    // 删除用户
    int delete(Integer userId);
}