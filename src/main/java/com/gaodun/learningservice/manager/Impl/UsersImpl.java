package com.gaodun.learningservice.manager.Impl;

import com.gaodun.learningservice.Entity.UsersEntity;
import com.gaodun.learningservice.manager.UsersManager;
import com.gaodun.learningservice.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户Manager实现类
 * @author shkstart
 */
@Slf4j
@Service
public class UsersImpl implements UsersManager {
    @Autowired
    private UsersMapper usersMapper;
    
    @Override
    public UsersEntity selectById(Integer userId) {
        log.info("selectById: {}", userId);
        return usersMapper.selectById(userId);
    }
    
    @Override
    public UsersEntity selectByUsername(String username) {
        log.info("selectByUsername: {}", username);
        return usersMapper.selectByUsername(username);
    }
    
    @Override
    public UsersEntity selectByEmail(String email) {
        log.info("selectByEmail: {}", email);
        return usersMapper.selectByEmail(email);
    }
    
    @Override
    public List<UsersEntity> selectByRole(String role) {
        log.info("selectByRole: {}", role);
        return usersMapper.selectByRole(role);
    }
    
    @Override
    public List<UsersEntity> selectAll() {
        log.info("selectAll");
        return usersMapper.selectAll();
    }
    
    @Override
    public int insert(UsersEntity user) {
        log.info("insert: {}", user);
        return usersMapper.insert(user);
    }
    
    @Override
    public int update(UsersEntity user) {
        log.info("update: {}", user);
        return usersMapper.update(user);
    }
    
    @Override
    public int delete(Integer userId) {
        log.info("delete: {}", userId);
        return usersMapper.delete(userId);
    }
}