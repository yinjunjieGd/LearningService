package com.gaodun.learningservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步任务配置类
 * @author shkstart
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 启用Spring的异步方法执行能力
}
