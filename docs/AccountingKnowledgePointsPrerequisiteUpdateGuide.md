# 初级会计实务知识点prerequisiteId字段更新指南

## 问题说明

发现知识点表（`knowledge_points`）中`course_id=4`（初级会计实务）的数据的`prerequisiteId`字段没有被完全补全，这会影响知识点之间的依赖关系展示和学习路径规划。

## 解决方案

为了解决这个问题，我们提供了专门的工具来更新初级会计实务知识点的`prerequisiteId`字段。这些工具基于预先定义的知识点依赖关系，可以自动恢复被清空的`prerequisiteId`字段。

## 使用方法

我们提供了三种方式来执行初级会计实务知识点`prerequisiteId`字段的更新操作：

### 方法1：运行测试类（推荐）

1. 找到并运行`AccountingKnowledgePointsPrerequisiteUpdateTest.java`测试类中的`updateAccountingKnowledgePointsPrerequisites()`方法
2. 该方法会自动执行以下操作：
   - 更新初级会计实务知识点的`prerequisiteId`字段
   - 验证更新结果

### 方法2：使用Maven命令

在项目根目录下打开命令提示符（CMD）或PowerShell，运行以下命令：

```powershell
mvn spring-boot:run -Dspring-boot.run.arguments=update-accounting-knowledge-points-prerequisites
```

### 方法3：仅验证更新结果

如果你只想验证初级会计实务知识点`prerequisiteId`字段的当前状态而不执行更新操作，可以：

1. 运行`AccountingKnowledgePointsPrerequisiteUpdateTest.java`测试类中的`verifyAccountingPrerequisiteRestoration()`方法

或者使用Maven命令：

```powershell
mvn spring-boot:run -Dspring-boot.run.arguments=verify-accounting-knowledge-points-prerequisites
```

## 技术细节

### 知识点依赖关系定义

初级会计实务知识点之间的依赖关系基于会计学习的逻辑顺序，定义在`AccountingKnowledgePointsPrerequisiteUpdater.java`文件的`buildAccountingPrerequisiteMap()`方法中。这些依赖关系与`AccountingKnowledgePointsGenerator.java`文件中定义的知识点生成逻辑一致。

### 更新逻辑

更新工具会执行以下操作：

1. 获取所有`course_id=4`的知识点
2. 遍历这些知识点，找出`prerequisiteId`为`NULL`的记录
3. 根据预定义的依赖关系映射，更新这些记录的`prerequisiteId`字段
4. 在更新前，工具会检查前置知识点是否存在，确保数据的完整性

### 验证结果

更新工具会提供以下验证信息：
- 初级会计实务知识点总数
- 已设置`prerequisiteId`的知识点数
- `prerequisiteId`仍为`NULL`的知识点数（这些知识点本身就是没有前置依赖的，如每个章节的第一个知识点）

## 常见问题解答

**Q**: 为什么有些初级会计实务知识点的`prerequisiteId`仍然为`NULL`？
**A**: 因为这些知识点本身就是没有前置依赖的，如每个章节的第一个知识点（如"会计概念与目标"、"货币资金"等）。

**Q**: 运行更新工具会影响其他课程的知识点吗？
**A**: 不会，更新工具只会处理`course_id=4`的知识点，不会影响其他课程的数据。

**Q**: 运行更新工具会影响已设置`prerequisiteId`的记录吗？
**A**: 不会，更新工具只会更新`prerequisiteId`为`NULL`的记录，不会覆盖已有的值。