# 火山引擎大模型API使用说明

## 概述
本项目已集成火山引擎大模型API，支持以下三种模型类型：
1. **文本推理** - 用于文本生成、对话等场景
2. **视觉理解** - 支持图片理解、图文问答等多模态场景
3. **图片生成** - 根据文本描述生成图片

## 配置步骤

### 1. 获取API密钥和模型ID
访问[火山方舟平台](https://console.volcengine.com/ark)，完成以下步骤：
- 注册并登录火山引擎账号
- 创建推理接入点，获取API Key
- 获取对应模型的Endpoint ID（Model ID）

### 2. 配置application.properties
在 `src/main/resources/application.properties` 中配置：

```properties
# 火山引擎API密钥
volcengine.api.key=YOUR_API_KEY_HERE

# API基础URL（默认即可）
volcengine.api.base-url=https://ark.cn-beijing.volces.com/api/v3

# 三种模型的Endpoint ID
volcengine.model.text=ep-20241226105107-xxxxx     # 文本推理模型ID
volcengine.model.vision=ep-20241226105107-xxxxx   # 视觉理解模型ID
volcengine.model.image=ep-20241226105107-xxxxx    # 图片生成模型ID
```

## API接口说明

### 1. 文本推理接口

**接口地址**: `POST /api/volcengine/text/inference`

**请求示例**:
```json
{
  "messages": [
    {
      "role": "system",
      "content": "你是一个有帮助的AI助手"
    },
    {
      "role": "user",
      "content": "请介绍一下Spring Boot"
    }
  ],
  "temperature": 0.7,
  "maxTokens": 2000,
  "topP": 0.9
}
```

**响应示例**:
```json
{
  "id": "chatcmpl-xxx",
  "object": "chat.completion",
  "created": 1703145600,
  "model": "ep-20241226105107-xxxxx",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "Spring Boot是一个基于Spring框架的..."
      },
      "finishReason": "stop"
    }
  ],
  "usage": {
    "promptTokens": 20,
    "completionTokens": 100,
    "totalTokens": 120
  }
}
```

### 2. 视觉理解接口

**接口地址**: `POST /api/volcengine/vision/understanding`

**请求示例**:
```json
{
  "messages": [
    {
      "role": "user",
      "content": [
        {
          "type": "text",
          "text": "这张图片中有什么？"
        },
        {
          "type": "image_url",
          "imageUrl": {
            "url": "https://example.com/image.jpg"
          }
        }
      ]
    }
  ],
  "temperature": 0.7,
  "maxTokens": 1000
}
```

**响应格式**: 与文本推理接口相同

### 3. 图片生成接口

**接口地址**: `POST /api/volcengine/image/generation`

**请求示例**:
```json
{
  "prompt": "一只可爱的小猫在草地上玩耍",
  "size": "1024x1024",
  "responseFormat": "url",
  "n": 1
}
```

**响应示例**:
```json
{
  "created": 1703145600,
  "data": [
    {
      "url": "https://example.com/generated-image.jpg"
    }
  ]
}
```

## 参数说明

### 通用参数
- `temperature`: 控制输出随机性，范围0-1，值越大越随机
- `maxTokens`: 最大生成token数
- `topP`: 核采样参数，范围0-1

### 图片生成参数
- `size`: 图片尺寸，支持 "1024x1024", "512x512" 等
- `responseFormat`: 返回格式，"url" 或 "b64_json"
- `n`: 生成图片数量

## 使用示例（Postman/CURL）

### 文本推理示例
```bash
curl -X POST http://localhost:8089/api/volcengine/text/inference \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {"role": "user", "content": "你好"}
    ],
    "temperature": 0.7
  }'
```

### 视觉理解示例
```bash
curl -X POST http://localhost:8089/api/volcengine/vision/understanding \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {
        "role": "user",
        "content": [
          {"type": "text", "text": "描述这张图片"},
          {"type": "image_url", "imageUrl": {"url": "https://example.com/image.jpg"}}
        ]
      }
    ]
  }'
```

### 图片生成示例
```bash
curl -X POST http://localhost:8089/api/volcengine/image/generation \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "一个美丽的日落",
    "size": "1024x1024",
    "responseFormat": "url"
  }'
```

## 注意事项

1. **API密钥安全**: 请勿将API密钥提交到代码仓库，建议使用环境变量或配置中心管理
2. **模型选择**: 不同模型有不同的能力和定价，请根据实际需求选择
3. **错误处理**: 接口调用失败时会返回500状态码，建议在客户端做好异常处理
4. **速率限制**: 请注意火山引擎的API调用频率限制
5. **Token消耗**: 每次API调用都会消耗token，请合理控制maxTokens参数

## 相关文档

- [火山方舟平台文档](https://www.volcengine.com/docs/82379)
- [火山引擎API接入指南](https://ycnuro1w1akp.feishu.cn/wiki/EJG6wNDxeiQkThkWOPncErc8nHh)

## 项目结构

```
src/main/java/com/gaodun/learningservice/
├── config/
│   └── VolcEngineConfig.java          # 配置类
├── DTO/
│   ├── VolcEngineTextRequest.java     # 文本推理请求DTO
│   ├── VolcEngineVisionRequest.java   # 视觉理解请求DTO
│   ├── VolcEngineImageRequest.java    # 图片生成请求DTO
│   └── VolcEngineResponse.java        # 统一响应DTO
├── manager/
│   └── VolcEngineService.java         # API调用服务类
└── controller/
    └── VolcEngineController.java      # REST控制器
```
