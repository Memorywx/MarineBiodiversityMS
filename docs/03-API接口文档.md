# 海洋生物多样性信息管理系统 — RESTful API 接口文档

**版本：** 1.0  
**日期：** 2026-04-21  
**Base URL：** `http://localhost:8080/api`  
**Content-Type：** `application/json`  
**认证方式：** `Authorization: Bearer <JWT Token>`

---

## 目录

1. [全局约定](#1-全局约定)
2. [认证模块](#2-认证模块-auth)
3. [用户管理模块](#3-用户管理模块-users)
4. [物种信息管理模块](#4-物种信息管理模块-species)
5. [生态系统管理模块](#5-生态系统管理模块-ecosystems)
6. [观测记录管理模块](#6-观测记录管理模块-observations)
7. [数据可视化与报表模块](#7-数据可视化与报表模块-dashboard)

---

## 1. 全局约定

### 1.1 统一响应结构
```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

### 1.2 分页请求参数
所有列表查询接口统一支持以下分页参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| current | int | 否 | 当前页码，默认 1 |
| size | int | 否 | 每页条数，默认 10 |

### 1.3 分页响应结构
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [ ... ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

---

## 2. 认证模块 (`/auth`)

### 2.1 用户注册
- **接口：** `POST /api/auth/register`
- **权限：** 公开
- **请求体：**
```json
{
  "username": "student01",
  "password": "123456",
  "email": "student01@example.com",
  "realName": "张三",
  "role": 2
}
```
- **字段说明：**
  - `role`：2-学生(student)，3-公众(public)。管理员/科研人员账号由后台直接创建，不走此接口。
- **响应：**
```json
{
  "code": 200,
  "message": "注册成功，请等待管理员审核",
  "data": null
}
```

### 2.2 用户登录
- **接口：** `POST /api/auth/login`
- **权限：** 公开
- **请求体：**
```json
{
  "username": "admin",
  "password": "123456"
}
```
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "username": "admin",
      "realName": "管理员",
      "avatar": "https://...",
      "role": 0
    }
  }
}
```

### 2.3 获取当前登录用户信息
- **接口：** `GET /api/auth/me`
- **权限：** 登录用户
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "管理员",
    "email": "admin@gdou.edu.cn",
    "avatar": "https://...",
    "role": 0,
    "status": 1
  }
}
```

---

## 3. 用户管理模块 (`/users`)

### 3.1 获取用户列表（管理员）
- **接口：** `GET /api/users`
- **权限：** ADMIN
- **查询参数：**
  - `keyword`（string，否）：按用户名/真实姓名模糊搜索
  - `role`（int，否）：按角色筛选
  - `status`（int，否）：按状态筛选
  - `current`、`size`：分页参数
- **响应：** 分页 `UserVO` 列表

### 3.2 获取用户详情
- **接口：** `GET /api/users/{id}`
- **权限：** ADMIN，或本人查看自己
- **响应：** `UserVO`

### 3.3 创建用户（管理员直接创建）
- **接口：** `POST /api/users`
- **权限：** ADMIN
- **请求体：**
```json
{
  "username": "researcher01",
  "password": "123456",
  "realName": "李教授",
  "email": "li@gdou.edu.cn",
  "role": 1
}
```

### 3.4 更新用户信息
- **接口：** `PUT /api/users/{id}`
- **权限：** ADMIN（可改所有字段），本人（仅能改基础信息）
- **请求体：**
```json
{
  "realName": "新姓名",
  "email": "new@example.com",
  "avatar": "https://..."
}
```

### 3.5 修改用户角色/状态（管理员）
- **接口：** `PUT /api/users/{id}/role-status`
- **权限：** ADMIN
- **请求体：**
```json
{
  "role": 1,
  "status": 1
}
```

### 3.6 删除用户
- **接口：** `DELETE /api/users/{id}`
- **权限：** ADMIN

### 3.7 审核用户（激活待审核账号）
- **接口：** `PUT /api/users/{id}/approve`
- **权限：** ADMIN
- **响应：** 状态变更为 1（正常）

---

## 4. 物种信息管理模块 (`/species`)

### 4.1 创建物种信息
- **接口：** `POST /api/species`
- **权限：** RESEARCHER / ADMIN
- **请求体：**
```json
{
  "chineseName": "中华白海豚",
  "scientificName": "Sousa chinensis",
  "phylum": "脊索动物门",
  "className": "哺乳纲",
  "orderName": "鲸偶蹄目",
  "family": "海豚科",
  "genus": "白海豚属",
  "species": "中华白海豚",
  "morphologicalFeatures": "体呈纺锤形，体色随年龄增长变浅...",
  "livingHabits": "栖息于河口、沿海浅水区...",
  "distribution": "中国东南沿海、东南亚...",
  "distributionLat": 21.2708,
  "distributionLng": 110.3594,
  "protectionLevel": "国家一级",
  "iucnStatus": "VU",
  "videoUrl": "https://...",
  "references": "[1] 某某文献...",
  "isPublic": 1,
  "images": [
    "https://image-server/xxx.jpg",
    "https://image-server/yyy.jpg"
  ]
}
```
- **响应：** 返回创建后的物种 `id`

### 4.2 更新物种信息
- **接口：** `PUT /api/species/{id}`
- **权限：** RESEARCHER（仅自己创建的）/ ADMIN
- **请求体：** 同创建，字段按需填写

### 4.3 删除物种信息
- **接口：** `DELETE /api/species/{id}`
- **权限：** RESEARCHER（仅自己创建的）/ ADMIN
- **业务规则：** 若该物种已被观测记录关联，返回 400 并提示先解除关联。

### 4.4 获取物种列表
- **接口：** `GET /api/species`
- **权限：** 登录用户均可查询；公众角色仅能看到 `isPublic=1` 的数据。
- **查询参数：**
  - `keyword`（string，否）：按中文名/学名模糊搜索
  - `phylum`、`className`、`orderName`、`family`（string，否）：分类层级筛选
  - `protectionLevel`（string，否）：保护等级
  - `iucnStatus`（string，否）：IUCN状态
  - `current`、`size`：分页
- **响应：** 分页 `SpeciesListVO`
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "chineseName": "中华白海豚",
        "scientificName": "Sousa chinensis",
        "phylum": "脊索动物门",
        "className": "哺乳纲",
        "protectionLevel": "国家一级",
        "iucnStatus": "VU",
        "coverImage": "https://image-server/xxx.jpg",
        "createTime": "2026-04-01T10:00:00"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

### 4.5 获取物种详情
- **接口：** `GET /api/species/{id}`
- **权限：** 登录用户均可；公众角色仅 `isPublic=1` 可见。
- **响应：** `SpeciesDetailVO`
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "chineseName": "中华白海豚",
    "scientificName": "Sousa chinensis",
    "phylum": "脊索动物门",
    "className": "哺乳纲",
    "orderName": "鲸偶蹄目",
    "family": "海豚科",
    "genus": "白海豚属",
    "species": "中华白海豚",
    "morphologicalFeatures": "...",
    "livingHabits": "...",
    "distribution": "...",
    "distributionLat": 21.2708,
    "distributionLng": 110.3594,
    "protectionLevel": "国家一级",
    "iucnStatus": "VU",
    "videoUrl": "...",
    "references": "...",
    "isPublic": 1,
    "images": [
      "https://image-server/xxx.jpg",
      "https://image-server/yyy.jpg"
    ],
    "createBy": 2,
    "createTime": "2026-04-01T10:00:00",
    "updateTime": "2026-04-10T15:30:00"
  }
}
```

### 4.6 物种分类层级列表（用于筛选下拉）
- **接口：** `GET /api/species/taxonomy`
- **权限：** 登录用户
- **查询参数：**
  - `level`（string，是）：`phylum`, `class`, `order`, `family`
  - `parent`（string，否）：上级分类值，用于级联筛选
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": ["脊索动物门", "节肢动物门", "软体动物门"]
}
```

---

## 5. 生态系统管理模块 (`/ecosystems`)

### 5.1 创建生态系统
- **接口：** `POST /api/ecosystems`
- **权限：** RESEARCHER / ADMIN
- **请求体：**
```json
{
  "name": "红树林",
  "type": "mangrove",
  "description": "热带、亚热带海岸潮间带特有的木本植物群落...",
  "geoRange": "中国东南沿海、海南岛...",
  "environmentFeatures": "高盐度、缺氧土壤、潮汐影响..."
}
```

### 5.2 更新生态系统
- **接口：** `PUT /api/ecosystems/{id}`
- **权限：** RESEARCHER / ADMIN

### 5.3 删除生态系统
- **接口：** `DELETE /api/ecosystems/{id}`
- **权限：** ADMIN
- **业务规则：** 若该生态系统下存在观测记录，不允许删除或转为逻辑删除（根据实现策略）。

### 5.4 获取生态系统列表
- **接口：** `GET /api/ecosystems`
- **权限：** 登录用户
- **查询参数：**
  - `keyword`（string，否）：按名称搜索
  - `current`、`size`：分页
- **响应：** 分页 `EcosystemVO` 列表

### 5.5 获取生态系统详情
- **接口：** `GET /api/ecosystems/{id}`
- **权限：** 登录用户
- **响应：** `EcosystemVO`

### 5.6 获取全部生态系统（用于下拉选择）
- **接口：** `GET /api/ecosystems/all`
- **权限：** 登录用户
- **响应：** `List<EcosystemVO>`（无分页，仅返回 id + name）

---

## 6. 观测记录管理模块 (`/observations`)

### 6.1 创建观测记录
- **接口：** `POST /api/observations`
- **权限：** RESEARCHER / ADMIN
- **请求体：**
```json
{
  "observationTime": "2026-03-15T08:30:00",
  "locationName": "湛江红树林保护区",
  "latitude": 21.2708,
  "longitude": 110.3594,
  "ecosystemId": 1,
  "observer": "张三、李四",
  "waterTemperature": 24.5,
  "salinity": 32.1,
  "phValue": 8.1,
  "depth": 2.5,
  "remarks": "天气晴朗，能见度良好",
  "speciesList": [
    {
      "speciesId": 1,
      "estimatedQuantity": 5,
      "behavior": "集群游动",
      "remarks": "成年个体"
    },
    {
      "speciesId": 3,
      "estimatedQuantity": 20,
      "behavior": "觅食",
      "remarks": ""
    }
  ]
}
```
- **响应：** 返回创建后的观测记录 `id`

### 6.2 更新观测记录
- **接口：** `PUT /api/observations/{id}`
- **权限：** RESEARCHER（仅自己创建的）/ ADMIN
- **请求体：** 同创建

### 6.3 删除观测记录
- **接口：** `DELETE /api/observations/{id}`
- **权限：** RESEARCHER（仅自己创建的）/ ADMIN

### 6.4 获取观测记录列表
- **接口：** `GET /api/observations`
- **权限：** 登录用户
- **查询参数：**
  - `keyword`（string，否）：按地点名称搜索
  - `ecosystemId`（long，否）：按生态系统筛选
  - `startTime`、`endTime`（string，否）：按观测时间范围筛选，格式 `yyyy-MM-dd`
  - `speciesId`（long，否）：按关联物种筛选
  - `current`、`size`：分页
- **响应：** 分页 `ObservationListVO`
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "observationTime": "2026-03-15T08:30:00",
        "locationName": "湛江红树林保护区",
        "latitude": 21.2708,
        "longitude": 110.3594,
        "ecosystemName": "红树林",
        "observer": "张三、李四",
        "speciesCount": 2,
        "createTime": "2026-03-15T20:00:00"
      }
    ],
    "total": 30,
    "size": 10,
    "current": 1,
    "pages": 3
  }
}
```

### 6.5 获取观测记录详情
- **接口：** `GET /api/observations/{id}`
- **权限：** 登录用户
- **响应：** `ObservationDetailVO`
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "observationTime": "2026-03-15T08:30:00",
    "locationName": "湛江红树林保护区",
    "latitude": 21.2708,
    "longitude": 110.3594,
    "ecosystemId": 1,
    "ecosystemName": "红树林",
    "observer": "张三、李四",
    "waterTemperature": 24.5,
    "salinity": 32.1,
    "phValue": 8.1,
    "depth": 2.5,
    "remarks": "天气晴朗...",
    "speciesList": [
      {
        "speciesId": 1,
        "chineseName": "中华白海豚",
        "scientificName": "Sousa chinensis",
        "estimatedQuantity": 5,
        "behavior": "集群游动",
        "remarks": "成年个体"
      }
    ],
    "createBy": 2,
    "createTime": "2026-03-15T20:00:00"
  }
}
```

---

## 7. 数据可视化与报表模块 (`/dashboard`)

### 7.1 获取综合看板关键指标
- **接口：** `GET /api/dashboard/stats`
- **权限：** 登录用户
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalSpecies": 128,
    "totalObservations": 56,
    "totalEcosystems": 8,
    "monthlyNewObservations": 12
  }
}
```

### 7.2 物种统计图表数据
- **接口：** `GET /api/dashboard/species-stats`
- **权限：** 登录用户
- **查询参数：**
  - `type`（string，是）：统计类型，`taxonomy`（分类占比）、`protection`（保护等级占比）、`iucn`（IUCN状态占比）
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    { "name": "脊索动物门", "value": 45 },
    { "name": "节肢动物门", "value": 38 },
    { "name": "软体动物门", "value": 30 }
  ]
}
```

### 7.3 观测统计图表数据
- **接口：** `GET /api/dashboard/observation-stats`
- **权限：** 登录用户
- **查询参数：**
  - `type`（string，是）：统计类型，`ecosystem`（各生态系统观测次数）、`timeline`（按时间趋势）、`observer`（按观测人员）
- **响应（timeline 示例）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "xAxis": ["2026-01", "2026-02", "2026-03", "2026-04"],
    "series": [5, 8, 12, 6]
  }
}
```

### 7.4 物种分布地图数据
- **接口：** `GET /api/dashboard/species-distribution`
- **权限：** 登录用户
- **查询参数：**
  - `speciesId`（long，否）：指定物种ID，不传则返回所有物种分布点
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "speciesId": 1,
      "chineseName": "中华白海豚",
      "scientificName": "Sousa chinensis",
      "lat": 21.2708,
      "lng": 110.3594
    }
  ]
}
```

### 7.5 观测地点地图数据
- **接口：** `GET /api/dashboard/observation-points`
- **权限：** 登录用户
- **查询参数：**
  - `ecosystemId`（long，否）：按生态系统筛选
  - `startTime`、`endTime`（string，否）：时间范围
- **响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "observationId": 1,
      "lat": 21.2708,
      "lng": 110.3594,
      "locationName": "湛江红树林保护区",
      "observationTime": "2026-03-15T08:30:00",
      "ecosystemName": "红树林",
      "speciesCount": 2
    }
  ]
}
```

### 7.6 数据导出
- **接口：** `GET /api/dashboard/export`
- **权限：** 登录用户（导出内容受权限过滤）
- **查询参数：**
  - `type`（string，是）：导出类型，`species`（物种数据）、`observation`（观测数据）
  - `format`（string，是）：`excel` 或 `pdf`
  - 各模块筛选参数同列表查询
- **响应：** 文件流下载（Content-Type: application/octet-stream）

---

## 8. DTO / VO 定义速查

### 8.1 公共分页参数 `PageParam`
```java
public class PageParam {
    private long current = 1;
    private long size = 10;
}
```

### 8.2 公共响应 `Result<T>`
```java
public class Result<T> {
    private int code;
    private String message;
    private T data;
}
```

### 8.3 核心 VO 字段速查
| VO | 核心字段 |
|----|---------|
| `UserVO` | id, username, realName, email, avatar, role, status, createTime |
| `SpeciesListVO` | id, chineseName, scientificName, phylum, className, protectionLevel, iucnStatus, coverImage, createTime |
| `SpeciesDetailVO` | SpeciesListVO 全部 + 详情字段 + images 列表 |
| `EcosystemVO` | id, name, type, description, geoRange, environmentFeatures |
| `ObservationListVO` | id, observationTime, locationName, lat, lng, ecosystemName, observer, speciesCount, createTime |
| `ObservationDetailVO` | ObservationListVO 全部 + 环境参数 + speciesList |
| `DashboardStatsVO` | totalSpecies, totalObservations, totalEcosystems, monthlyNewObservations |
