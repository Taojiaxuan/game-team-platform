-- =============================================================
-- 游戏战队与赛事报名平台 数据库初始化脚本
-- 执行方式：mysql -uroot -p < init.sql
-- =============================================================

CREATE DATABASE IF NOT EXISTS game_team DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE game_team;

-- 确保客户端以 utf8mb4 传输，避免中文乱码
SET NAMES utf8mb4;

-- ---------------------------------------------------------
-- 战队表
-- ---------------------------------------------------------
DROP TABLE IF EXISTS registration;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS tournament;
DROP TABLE IF EXISTS team;

CREATE TABLE team (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name          VARCHAR(64)  NOT NULL COMMENT '战队名称',
    tag           VARCHAR(128)          DEFAULT NULL COMMENT '战队标签/口号',
    captain_name  VARCHAR(32)  NOT NULL COMMENT '队长姓名',
    contact_phone VARCHAR(20)           DEFAULT NULL COMMENT '联系方式',
    description   VARCHAR(500)          DEFAULT NULL COMMENT '战队介绍',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_team_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='战队表';

-- ---------------------------------------------------------
-- 队员表
-- ---------------------------------------------------------
CREATE TABLE member (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    team_id     BIGINT       NOT NULL COMMENT '所属战队ID',
    name        VARCHAR(32)  NOT NULL COMMENT '姓名',
    game_id     VARCHAR(64)           DEFAULT NULL COMMENT '游戏昵称/ID',
    role        VARCHAR(16)  NOT NULL DEFAULT '队员' COMMENT '队内角色',
    phone       VARCHAR(20)           DEFAULT NULL COMMENT '联系方式',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_member_team (team_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='队员表';

-- ---------------------------------------------------------
-- 赛事表
-- ---------------------------------------------------------
CREATE TABLE tournament (
    id                    BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    name                  VARCHAR(128) NOT NULL COMMENT '赛事名称',
    game_name             VARCHAR(64)  NOT NULL COMMENT '游戏项目',
    start_time            DATETIME              DEFAULT NULL COMMENT '比赛开始时间',
    end_time              DATETIME              DEFAULT NULL COMMENT '比赛结束时间',
    registration_deadline DATETIME              DEFAULT NULL COMMENT '报名截止时间',
    max_teams             INT          NOT NULL DEFAULT 16 COMMENT '报名战队数量上限',
    team_size             INT          NOT NULL DEFAULT 5 COMMENT '每队人数要求',
    status                INT          NOT NULL DEFAULT 0 COMMENT '状态 0未开始 1报名中 2进行中 3已结束',
    description           VARCHAR(500)          DEFAULT NULL COMMENT '赛事描述',
    create_time           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='赛事表';

-- ---------------------------------------------------------
-- 报名表
-- ---------------------------------------------------------
CREATE TABLE registration (
    id            BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    tournament_id BIGINT   NOT NULL COMMENT '赛事ID',
    team_id       BIGINT   NOT NULL COMMENT '战队ID',
    status        INT      NOT NULL DEFAULT 0 COMMENT '报名状态 0待审核 1通过 2拒绝',
    register_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tournament_team (tournament_id, team_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='报名表';

-- =============================================================
-- 种子数据
-- =============================================================

INSERT INTO team (name, tag, captain_name, contact_phone, description) VALUES
('峡谷王者', '王者荣耀·王者段位', '张伟', '13800000001', '一支追求极致的王者荣耀战队'),
('夜鹰战队', '英雄联盟·峡谷之巅', '李娜', '13800000002', '夜鹰出击，战无不胜'),
('星辰之翼', '和平精英·百战精英', '王强', '13800000003', '星辰大海，勇往直前');

INSERT INTO member (team_id, name, game_id, role, phone) VALUES
(1, '张伟', 'XGWang', '队长', '13800000001'),
(1, '刘洋', 'XGLiu', '副队长', '13800000011'),
(1, '陈晨', 'XGChen', '队员', '13800000012'),
(1, '赵磊', 'XGZhao', '队员', '13800000013'),
(1, '孙悦', 'XGSun', '队员', '13800000014'),
(2, '李娜', 'YYLi', '队长', '13800000002'),
(2, '周杰', 'YYZhou', '副队长', '13800000021'),
(2, '吴昊', 'YYWu', '队员', '13800000022'),
(2, '郑爽', 'YYZheng', '队员', '13800000023'),
(2, '冯军', 'YYFeng', '队员', '13800000024'),
(3, '王强', 'XCWang', '队长', '13800000003'),
(3, '杨雪', 'XCYang', '副队长', '13800000031'),
(3, '何俊', 'XCHe', '队员', '13800000032'),
(3, '高翔', 'XCGao', '队员', '13800000033'),
(3, '罗丹', 'XCLuo', '队员', '13800000034');

-- 报名截止时间使用相对时间，保证演示时始终有效
INSERT INTO tournament (name, game_name, start_time, end_time, registration_deadline, max_teams, team_size, status, description) VALUES
('2026 校园王者荣耀春季赛', '王者荣耀', DATE_ADD(NOW(), INTERVAL 20 DAY), DATE_ADD(NOW(), INTERVAL 22 DAY), DATE_ADD(NOW(), INTERVAL 15 DAY), 16, 5, 1, '面向全校学生，5V5 征召模式，冠军队伍奖金 3000 元'),
('英雄联盟 S 赛校内选拔赛', '英雄联盟', DATE_ADD(NOW(), INTERVAL 40 DAY), DATE_ADD(NOW(), INTERVAL 45 DAY), DATE_ADD(NOW(), INTERVAL 35 DAY), 8, 5, 1, '校内选拔赛，胜出队伍晋级市赛'),
('和平精英百人竞技联赛', '和平精英', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 20, 4, 3, '已结束的示例赛事');

INSERT INTO registration (tournament_id, team_id, status) VALUES
(1, 1, 1),
(1, 2, 0),
(2, 1, 1);
