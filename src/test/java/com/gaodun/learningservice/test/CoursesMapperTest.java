package com.gaodun.learningservice.test;

import com.gaodun.learningservice.Entity.CoursesEntity;
import com.gaodun.learningservice.mapper.CoursesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 测试CoursesMapper的selectAll方法是否正常工作
 * 用于验证修复的Invalid bound statement (not found)错误
 */
@SpringBootTest
public class CoursesMapperTest {

    @Autowired
    private CoursesMapper coursesMapper;

    @Test
    public void testSelectAll() {
        System.out.println("开始测试CoursesMapper.selectAll方法...");
        List<CoursesEntity> courses = coursesMapper.selectAll();
        System.out.println("查询成功，共获取到" + courses.size() + "个课程");
        // 打印前3个课程的信息
        int displayCount = Math.min(3, courses.size());
        for (int i = 0; i < displayCount; i++) {
            CoursesEntity course = courses.get(i);
            System.out.println("课程ID: " + course.getId() + ", 课程标题: " + course.getTitle());
        }
        // 验证查询结果不为空
        assert courses != null;
        System.out.println("CoursesMapper.selectAll方法测试成功！");
    }
}