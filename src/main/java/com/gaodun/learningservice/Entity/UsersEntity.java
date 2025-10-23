package com.gaodun.learningservice.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户实体类
 * @author shkstart
 */
@Data
@Entity
@Table(name = "users")
public class UsersEntity {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "create_time")
    private java.util.Date createTime;
    
    @Column(name = "last_login_time")
    private java.util.Date lastLoginTime;
    
    @Column(name = "status")
    private String status;
}