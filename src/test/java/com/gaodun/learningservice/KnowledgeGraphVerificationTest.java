package com.gaodun.learningservice;

import com.gaodun.learningservice.Entity.UserLearningProgressEntity;
import com.gaodun.learningservice.mapper.UserLearningProgressMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 知识图谱数据验证测试类
 */
@SpringBootTest
public class KnowledgeGraphVerificationTest {

    @Autowired
    private UserLearningProgressMapper userLearningProgressMapper;

    /**
     * 验证知识图谱数据是否已保存到数据库
     */
    @Test
    public void testVerifyKnowledgeGraphData() {
        System.out.println("\n========== 验证知识图谱数据 ==========");
        
        // 查询用户ID=1, 课程ID=1的学习进度记录
        UserLearningProgressEntity progress = 
            userLearningProgressMapper.selectByUserIdAndCourseId(1L, 1L);
        
        if (progress == null) {
            System.out.println("✗ 未找到学习进度记录");
            fail("用户学习进度记录不存在");
            return;
        }
        
        System.out.println("找到学习进度记录:");
        System.out.println("  - 进度ID: " + progress.getId());
        System.out.println("  - 用户ID: " + progress.getUserId());
        System.out.println("  - 课程ID: " + progress.getCourseId());
        System.out.println("  - 更新时间: " + progress.getUpdateTime());
        
        String knowledgePic = progress.getKnowledgePic();
        if (knowledgePic != null && !knowledgePic.isEmpty()) {
            System.out.println("  - 知识图谱数据长度: " + knowledgePic.length() + " 字符");
            System.out.println("  - 数据前缀: " + knowledgePic.substring(0, Math.min(50, knowledgePic.length())));
            
            // 验证是否是base64格式
            if (knowledgePic.startsWith("data:image") || knowledgePic.startsWith("iVBOR") || knowledgePic.startsWith("/9j/")) {
                System.out.println("\n✓ 知识图谱数据已成功保存到数据库");
                System.out.println("✓ 数据格式正确（base64图片数据）");
            } else {
                System.out.println("\n⚠ 警告: 数据格式可能不正确");
            }
        } else {
            System.out.println("  - 知识图谱数据: 未设置或为空");
            System.out.println("\n✗ 知识图谱数据未保存或为空");
            fail("知识图谱数据未保存到数据库");
        }
        
        System.out.println("=========================================\n");
    }
}
