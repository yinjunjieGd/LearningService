package com.gaodun.learningservice;

import com.gaodun.learningservice.Entity.AnswerRecordsEntity;
import com.gaodun.learningservice.Entity.UserLearningProfilesEntity;
import com.gaodun.learningservice.Entity.UserLearningRecordsEntity;
import com.gaodun.learningservice.mapper.AnswerRecordsMapper;
import com.gaodun.learningservice.mapper.UserLearningProfilesMapper;
import com.gaodun.learningservice.mapper.UserLearningRecordsMapper;
import com.gaodun.learningservice.service.AnswerRecordAsyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 答题记录异步处理服务测试类
 * 
 * 测试答题记录更新后自动触发知识点掌握度分析并保存到用户学习画像
 */
@SpringBootTest
public class AnswerRecordAsyncServiceTest {

    @Autowired
    private AnswerRecordAsyncService answerRecordAsyncService;

    @Autowired
    private AnswerRecordsMapper answerRecordsMapper;

    @Autowired
    private UserLearningRecordsMapper userLearningRecordsMapper;

    @Autowired
    private UserLearningProfilesMapper userLearningProfilesMapper;

    /**
     * 测试完整的答题记录处理流程
     * 1. 创建测试答题记录
     * 2. 触发异步处理
     * 3. 验证用户学习画像是否正确生成
     */
    @Test
    public void testAnswerRecordProcessing() throws InterruptedException {
        System.out.println("\n========== 测试答题记录异步处理 ==========");
        
        // 1. 查询现有的答题记录用于测试
        List<AnswerRecordsEntity> existingRecords = answerRecordsMapper.selectAll();
        
        if (existingRecords == null || existingRecords.isEmpty()) {
            System.out.println("⚠ 警告: 数据库中没有答题记录，跳过测试");
            System.out.println("建议: 先插入一些测试数据后再运行此测试");
            return;
        }
        
        // 使用第一条答题记录进行测试
        AnswerRecordsEntity testRecord = existingRecords.get(0);
        Integer recordId = testRecord.getRecordId();
        Integer userId = testRecord.getUserId();
        
        System.out.println("使用测试数据:");
        System.out.println("  - 答题记录ID: " + recordId);
        System.out.println("  - 用户ID: " + userId);
        
        // 2. 查询该用户的学习记录数量
        List<UserLearningRecordsEntity> learningRecords = 
            userLearningRecordsMapper.selectByUserId(userId);
        
        System.out.println("\n关联的学习记录数: " + 
            (learningRecords != null ? learningRecords.size() : 0));
        
        if (learningRecords == null || learningRecords.isEmpty()) {
            System.out.println("⚠ 警告: 该用户没有关联的学习记录，无法进行掌握度分析");
            return;
        }
        
        // 3. 删除之前可能存在的学习画像（用于测试）
        UserLearningProfilesEntity existingProfile = 
            userLearningProfilesMapper.selectByUserId(userId);
        if (existingProfile != null) {
            System.out.println("\n清理之前的学习画像数据...");
            userLearningProfilesMapper.deleteByUserId(userId);
        }
        
        // 4. 触发异步处理
        System.out.println("\n触发答题记录异步处理...");
        answerRecordAsyncService.processAnswerRecordUpdate(recordId);
        
        // 5. 等待异步处理完成（实际场景中通过消息队列或事件驱动）
        System.out.println("等待异步处理完成（90秒，因为需要多次调用AI API）...");
        for (int i = 1; i <= 90; i++) {
            Thread.sleep(1000);
            if (i % 5 == 0) {
                System.out.println("已等待 " + i + " 秒...");
                // 中间检查是否已生成
                UserLearningProfilesEntity tempProfile = userLearningProfilesMapper.selectByUserId(userId);
                if (tempProfile != null) {
                    System.out.println("✓ 学习画像已在第 " + i + " 秒时生成！");
                    break;
                }
            }
        }
        
        // 6. 验证学习画像是否生成
        System.out.println("\n验证学习画像生成结果:");
        UserLearningProfilesEntity profile = userLearningProfilesMapper.selectByUserId(userId);
        
        if (profile != null) {
            System.out.println("✓ 学习画像已生成");
            System.out.println("  - Profile ID: " + profile.getProfileId());
            System.out.println("  - 用户ID: " + profile.getUserId());
            System.out.println("  - 课程ID: " + profile.getCourseId());
            System.out.println("  - 掌握度数据长度: " + 
                (profile.getMasteryLevel() != null ? profile.getMasteryLevel().length() : 0) + " 字符");
            System.out.println("  - 更新时间: " + profile.getLastUpdated());
            
            // 验证掌握度数据不为空
            assertNotNull(profile.getMasteryLevel(), "掌握度数据不应为空");
            assertTrue(profile.getMasteryLevel().length() > 0, "掌握度数据应包含内容");
            
            // 验证JSON格式（简单检查）
            String masteryLevel = profile.getMasteryLevel();
            assertTrue(masteryLevel.startsWith("{") && masteryLevel.endsWith("}"), 
                "掌握度数据应为JSON格式");
            
            System.out.println("\n掌握度数据示例（前200字符）:");
            System.out.println(masteryLevel.substring(0, Math.min(200, masteryLevel.length())));
            if (masteryLevel.length() > 200) {
                System.out.println("... (共 " + masteryLevel.length() + " 字符)");
            }
            
            System.out.println("\n✓ 测试通过! 答题记录异步处理功能正常");
        } else {
            System.out.println("✗ 测试失败: 学习画像未生成");
            System.out.println("可能原因:");
            System.out.println("  1. 异步处理时间不够（需要调用AI API）");
            System.out.println("  2. AI API调用失败");
            System.out.println("  3. 数据库保存失败");
            System.out.println("\n请查看应用日志以获取详细错误信息");
            
            fail("学习画像未成功生成");
        }
        
        System.out.println("=========================================\n");
    }
    
    /**
     * 测试查询用户学习画像
     */
    @Test
    public void testQueryUserLearningProfile() {
        System.out.println("\n========== 测试查询用户学习画像 ==========");
        
        // 查询所有学习画像
        List<UserLearningProfilesEntity> allProfiles = userLearningProfilesMapper.selectAll();
        
        System.out.println("数据库中的学习画像总数: " + 
            (allProfiles != null ? allProfiles.size() : 0));
        
        if (allProfiles != null && !allProfiles.isEmpty()) {
            System.out.println("\n学习画像列表:");
            for (UserLearningProfilesEntity profile : allProfiles) {
                System.out.println("  - Profile ID: " + profile.getProfileId() + 
                    ", 用户ID: " + profile.getUserId() + 
                    ", 课程ID: " + profile.getCourseId() + 
                    ", 更新时间: " + profile.getLastUpdated());
                
                // 显示掌握度数据摘要
                String masteryLevel = profile.getMasteryLevel();
                if (masteryLevel != null) {
                    System.out.println("    掌握度数据: " + 
                        masteryLevel.substring(0, Math.min(100, masteryLevel.length())) + 
                        (masteryLevel.length() > 100 ? "..." : ""));
                }
            }
            System.out.println("\n✓ 查询成功");
        } else {
            System.out.println("⚠ 数据库中暂无学习画像数据");
        }
        
        System.out.println("=========================================\n");
    }
}
