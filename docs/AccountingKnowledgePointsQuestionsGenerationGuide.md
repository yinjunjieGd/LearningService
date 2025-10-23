# 初级会计实务知识点题目生成与插入指南

## 1. 功能说明

本指南提供了一套完整的工具，用于**自动为初级会计实务的知识点生成题目并插入到questions表中**。

工具会自动：
- 读取数据库中course_id=4的知识点数据
- 为每个知识点生成3-5道题目（单选题为主）
- 自动插入生成的题目到questions表中
- 提供多种执行方式和验证机制

## 2. 使用前提

在使用本工具前，请确保：

1. **已成功插入初级会计实务知识点数据**（course_id=4）
   - 如果尚未插入知识点数据，请先参考`AccountingKnowledgePointsDataInsertGuide.md`文档
2. 确保项目环境配置正确，能够连接到数据库
3. 确保questions表已存在且结构正确

## 3. 工具组件说明

本工具包含以下核心组件：

| 组件名称 | 类型 | 路径 | 功能描述 |
|---------|------|------|---------|
| AccountingKnowledgePointsQuestionsGenerator | 核心工具类 | src/main/java/com/gaodun/learningservice/utils/ | 负责生成题目并插入数据库 |
| AccountingKnowledgePointsQuestionsGenerationTest | 测试类 | src/test/java/com/gaodun/learningservice/test/ | 提供测试方法执行题目生成 |
| AccountingKnowledgePointsQuestionsGenerationRunner | 命令行运行器 | src/main/java/com/gaodun/learningservice/runner/ | 通过命令行参数触发题目生成 |
| AccountingKnowledgePointsQuestionsInserter | 独立入口类 | src/main/java/com/gaodun/learningservice/utils/ | 包含main方法的独立执行入口 |
| generate_accounting_knowledge_points_questions.bat | 批处理文件 | 项目根目录 | 双击即可执行题目生成的批处理脚本 |

## 4. 使用方法

### 4.1 方法一：使用批处理文件（推荐）

这是最简便的方法，适合所有用户：

1. 确保已成功插入初级会计实务知识点数据
2. 找到项目根目录下的 `generate_accounting_knowledge_points_questions.bat` 文件
3. 双击运行该批处理文件
4. 等待执行完成，查看控制台输出的结果

### 4.2 方法二：运行测试类

适合开发人员在IDE中直接运行：

1. 打开IDE（如IntelliJ IDEA或Eclipse）
2. 找到测试类 `AccountingKnowledgePointsQuestionsGenerationTest`
3. 运行 `testGenerateAndInsertQuestionsForAccountingKnowledgePoints()` 方法
4. 查看控制台输出的结果

### 4.3 方法三：使用Maven命令执行独立入口类

适合熟悉Maven的用户：

1. 打开命令行终端
2. 切换到项目根目录
3. 执行以下命令：
   ```bash
   mvn exec:java -Dexec.mainClass="com.gaodun.learningservice.utils.AccountingKnowledgePointsQuestionsInserter"
   ```
4. 等待执行完成，查看控制台输出的结果

### 4.4 方法四：通过命令行运行器执行

适合通过Spring Boot应用启动参数执行：

1. 编译项目
2. 使用以下命令启动应用：
   ```bash
   java -jar your-application.jar generate-accounting-questions
   ```
3. 等待执行完成，查看控制台输出的结果

## 5. 数据生成规则

系统会根据以下规则生成题目：

- **题目ID**：从1000开始，依次递增
- **题目类型**：主要为单选题（single_choice）
- **题目数量**：每个知识点生成3-5道题目
- **题干内容**：根据知识点标题和描述自动生成，存储在stem字段
- **选项设置**：生成4个选项，以Map<String, String>形式存储在options字段（JSON格式），其中第一个选项为正确答案
- **正确答案**：存储在correct_answer字段
- **AI分析**：存储在ai_analysis字段
- **创建时间**：自动设置为当前时间，存储在created_at字段

## 6. 验证数据

生成题目后，系统会自动验证数据是否成功插入。您也可以通过以下方式手动验证：

### 6.1 使用测试类验证

运行 `AccountingKnowledgePointsQuestionsGenerationTest` 类中的 `testVerifyQuestionsInsertion()` 方法

### 6.2 使用命令行运行器验证

通过Spring Boot应用启动参数执行：
```bash
java -jar your-application.jar verify-accounting-questions
```

### 6.3 直接查询数据库

使用SQL查询：
```sql
-- 查询所有通过此工具生成的题目
SELECT COUNT(*) FROM questions WHERE question_id >= 1000;

-- 查询特定知识点的题目
SELECT * FROM questions WHERE point_id = 特定知识点ID;
```

## 7. 注意事项

1. **避免重复插入**：工具会自动检查是否已存在相关题目，如果已存在则不会重复插入
2. **知识点依赖**：题目生成依赖于已存在的初级会计实务知识点数据（course_id=4）
3. **数据结构一致性**：QuestionsEntity实体类已与questions表结构保持一致，直接使用实体类字段进行数据操作
4. **日志输出**：工具会输出详细的日志信息，便于排查问题

## 8. 常见问题解答

### Q1: 执行批处理文件时出现"编译失败"怎么办？

**A1**: 请检查项目代码是否存在编译错误，修复后重新执行批处理文件。

### Q2: 生成的题目数量与预期不符怎么办？

**A2**: 题目数量取决于知识点数量和每个知识点生成的题目数（3-5道），如果知识点数量不足，生成的题目数量也会相应减少。

### Q3: 如何修改生成题目时的题目类型、难度或分数？

**A3**: 可以修改 `AccountingKnowledgePointsQuestionsGenerator.java` 文件中的相关设置，如题目类型、难度和分数等参数。

### Q4: 生成的题目质量如何？

**A4**: 目前生成的题目是基于知识点标题和描述自动生成的，可能需要人工审核和调整，以确保题目的准确性和合理性。

## 9. 联系与支持

如果在使用过程中遇到任何问题，请联系技术支持。