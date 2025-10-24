# 知识图谱图片查询接口文档

## 接口概述

本接口用于根据用户ID和课程ID查询对应的知识图谱图片数据，图片数据存储在`user_learning_progress`表的`knowledge_pic`字段中。

## 接口信息

- **接口路径**: `/api/study-plan/knowledge-pic`
- **请求方法**: `POST`
- **接口描述**: 根据用户ID和课程ID查询知识图谱图片

## 请求参数

| 参数名 | 类型 | 是否必选 | 描述 | 示例值 |
|-------|------|----------|------|--------|
| userId | Long | 是 | 用户ID | 1001 |
| courseId | Long | 是 | 课程ID | 2001 |

## 请求示例

```
POST /api/study-plan/knowledge-pic?userId=1001&courseId=2001
```

## 响应参数

接口返回统一格式的JSON响应，包含以下字段：

| 字段名 | 类型 | 描述 |
|-------|------|------|
| success | Boolean | 请求是否成功 |
| message | String | 响应消息 |
| data | String | 知识图谱图片数据（Base64编码或图片URL） |

## 响应示例

### 成功响应

```json
{
  "success": true,
  "message": "知识图谱图片获取成功",
  "data": "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAA..."  // Base64编码的图片数据
}
```

### 失败响应 - 数据不存在

```json
{
  "success": false,
  "message": "未找到对应用户的学习进度数据"
}
```

### 失败响应 - 图片数据为空

```json
{
  "success": false,
  "message": "知识图谱图片数据不存在"
}
```

### 失败响应 - 系统异常

```json
{
  "success": false,
  "message": "获取知识图谱图片失败: [具体错误信息]"
}
```

## 错误码说明

| HTTP状态码 | 描述 |
|-----------|------|
| 200 | 请求成功 |
| 404 | 未找到数据或图片不存在 |
| 500 | 服务器内部错误 |

## 注意事项

1. 接口使用POST方法，参数通过URL参数传递
2. `knowledge_pic`字段存储的是长文本类型数据，通常为Base64编码的图片或图片URL
3. 在调用接口前，请确保用户ID和课程ID正确，并且存在对应的学习进度记录
4. 如果需要展示图片，请根据`data`字段的格式进行相应的解码或展示处理