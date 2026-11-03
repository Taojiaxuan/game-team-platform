# 游戏战队与赛事报名平台

一个基于 Spring Boot 的游戏战队与赛事报名管理系统，围绕 **战队（Team）/ 队员（Member）/ 赛事（Tournament）/ 报名（Registration）** 四个核心实体，提供完整的增删改查与报名审核流程。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Spring Boot 3.2 · MyBatis-Plus 3.5 · MySQL 8 |
| 前端 | 原生 HTML + CSS + JavaScript（无构建，单文件） |
| 其它 | Lombok · Jackson（统一时间格式） |

## 功能清单

- **战队管理**：战队增删改查，列表附带队员数量，删除时级联清理队员与报名记录。
- **队员管理**：队员增删改查，可按战队筛选，队内角色（队长/副队长/队员）。
- **赛事管理**：赛事增删改查，含开始/结束时间、报名截止、每队人数、战队上限、状态（未开始/报名中/进行中/已结束）。
- **报名管理**：战队报名、审核（通过/拒绝），报名时校验业务规则。

## 目录结构

```
campus_lost_platform
├── pom.xml
├── src/main/java/com/gameteam
│   ├── GameTeamApplication.java      # 启动类
│   ├── config/                       # CORS / Jackson 配置
│   ├── common/                       # 统一返回、异常处理
│   ├── entity/                       # Team / Member / Tournament / Registration
│   ├── mapper/                       # MyBatis-Plus Mapper
│   ├── service/                      # 业务逻辑（含报名校验规则）
│   ├── controller/                   # REST 接口
│   └── vo/                           # TeamVO / RegistrationVO
├── src/main/resources
│   ├── application.yml               # 数据源等配置
│   └── sql/init.sql                  # 建库建表 + 种子数据
└── frontend/index.html               # 前端单页（直接浏览器打开）
```

## 快速开始

### 1. 初始化数据库

```bash
mysql -uroot -p < src/main/resources/sql/init.sql
```

脚本会自动创建 `game_team` 库、四张表并写入演示数据（报名截止时间用相对时间，保证演示时始终有效）。

### 2. 修改数据库配置

编辑 `src/main/resources/application.yml`，把用户名/密码改成你的：

```yaml
spring:
  datasource:
    username: root
    password: root
```

### 3. 启动后端

在 IntelliJ IDEA 中直接运行 `GameTeamApplication`，或命令行：

```bash
mvn spring-boot:run
```

后端默认端口 `8080`。

### 4. 打开前端

直接用浏览器打开 `frontend/index.html`（无需部署），即可看到四个管理模块。

> 前端请求地址写死为 `http://localhost:8080`，如后端端口不同请修改 `index.html` 顶部的 `BASE` 常量。

## 接口清单

| 模块 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 战队 | GET | `/api/team/list` | 战队列表（含人数） |
| 战队 | GET | `/api/team/{id}` | 战队详情 |
| 战队 | POST | `/api/team` | 新建战队 |
| 战队 | PUT | `/api/team` | 更新战队 |
| 战队 | DELETE | `/api/team/{id}` | 删除战队（级联） |
| 队员 | GET | `/api/member/list?teamId=` | 队员列表（可按战队筛选） |
| 队员 | POST | `/api/member` | 新增队员 |
| 队员 | PUT | `/api/member` | 更新队员 |
| 队员 | DELETE | `/api/member/{id}` | 删除队员 |
| 赛事 | GET | `/api/tournament/list?status=` | 赛事列表（可按状态筛选） |
| 赛事 | POST | `/api/tournament` | 新建赛事 |
| 赛事 | PUT | `/api/tournament` | 更新赛事 |
| 赛事 | DELETE | `/api/tournament/{id}` | 删除赛事（级联） |
| 报名 | GET | `/api/registration/list` | 报名列表（含赛事/战队信息） |
| 报名 | POST | `/api/registration/register?tournamentId=&teamId=` | 战队报名 |
| 报名 | PUT | `/api/registration/review/{id}?status=` | 审核（1 通过 / 2 拒绝） |
| 报名 | DELETE | `/api/registration/{id}` | 删除报名记录 |

所有接口统一返回 `{ code, message, data }` 结构，`code=200` 表示成功。

## 报名业务规则

战队报名某赛事时，会依次校验：

1. 赛事存在，且状态为「报名中」（`status=1`）；
2. 未过报名截止时间；
3. 战队存在；
4. 战队队员人数满足赛事「每队人数要求」；
5. 已报名（未拒绝）的战队数量未达「报名上限」；
6. 同一战队不能重复报名同一赛事。

任一规则不满足，接口会返回明确的错误提示（如「队员人数不足，该赛事要求每队至少 5 人，当前 3 人」）。
