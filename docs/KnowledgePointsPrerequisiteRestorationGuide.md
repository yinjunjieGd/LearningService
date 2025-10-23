# 知识点表prerequisiteId字段恢复指南

## 问题说明

在执行数据清理操作时，发现知识点表（`knowledge_points`）中的`prerequisiteId`字段全部变成了`NULL`。经过分析，发现问题出在`SimpleDataCleanupAndCourseGenerator.java`文件中的数据清理逻辑。

**问题根源**：该文件中的`cleanupDuplicateKnowledgePoints()`方法在处理重复知识点时，会先执行以下SQL语句：
```sql
UPDATE knowledge_points SET prerequisite_id = NULL
```

这条语句会清空所有知识点的前置依赖ID，导致知识点之间的依赖关系丢失。

## 解决方案

为了解决这个问题，我们提供了以下解决方案：

### 1. 修复数据清理逻辑

我们已经修改了`SimpleDataCleanupAndCourseGenerator.java`文件中的数据清理方法，使用更高效的SQL语句直接删除重复数据，不再清空`prerequisiteId`字段。

### 2. 创建知识点前置依赖恢复工具

我们创建了`KnowledgePointsPrerequisiteRestorer`工具类，用于恢复被清空的`prerequisiteId`字段。该工具类包含了所有知识点之间的依赖关系定义，可以根据这些定义恢复知识点表中的`prerequisiteId`字段。

## 使用方法

### 步骤1：运行知识点前置依赖恢复测试

1. 找到并运行`KnowledgePointsPrerequisiteRestorationTest.java`测试类中的`restoreKnowledgePointsPrerequisites()`方法
2. 该方法会自动执行以下操作：
   - 恢复知识点表中的`prerequisiteId`字段
   - 验证恢复结果

### 步骤2：验证恢复结果

运行`verifyPrerequisiteRestoration()`方法可以验证知识点表中`prerequisiteId`字段的恢复情况，不会执行任何修改操作。

## 防止问题再次发生

修改后的`SimpleDataCleanupAndCourseGenerator.java`文件已经解决了导致`prerequisiteId`字段被清空的问题。现在，数据清理操作不会再影响知识点之间的依赖关系。

## 技术细节

### 知识点依赖关系定义

知识点之间的依赖关系定义在`KnowledgePointsPrerequisiteRestorer.java`文件的`buildPrerequisiteMap()`方法中。该方法从原有的`KnowledgePointsGenerator.java`和`AccountingKnowledgePointsGenerator.java`文件中提取了所有知识点之间的依赖关系。

### 恢复逻辑

恢复工具会遍历所有`prerequisiteId`为`NULL`的知识点记录，根据预定义的依赖关系映射，更新这些记录的`prerequisiteId`字段。在更新前，工具会检查前置知识点是否存在，确保数据的完整性。

## 验证恢复是否成功

恢复工具提供了`verifyPrerequisiteRestoration()`方法，可以统计和显示以下信息：
- 总知识点数
- 已设置`prerequisiteId`的知识点数
- `prerequisiteId`仍为`NULL`的知识点数（这些知识点本身就是没有前置依赖的）

## 常见问题解答

**Q**: 为什么有些知识点的`prerequisiteId`仍然为`NULL`？
**A**: 因为这些知识点本身就是没有前置依赖的，如每个课程的第一个知识点。

**Q**: 运行恢复工具会影响其他数据吗？
**A**: 不会，恢复工具只会更新`prerequisiteId`为`NULL`的记录，不会影响其他数据。

**Q**: 如何避免以后再次出现这个问题？
**A**: 请使用修改后的`SimpleDataCleanupAndCourseGenerator.java`文件进行数据清理操作，该文件不会再清空`prerequisiteId`字段。