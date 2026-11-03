package com.gameteam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 赛事
 */
@Data
@TableName("tournament")
public class Tournament {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 赛事名称 */
    private String name;

    /** 游戏项目，如：王者荣耀 / 英雄联盟 / CS2 */
    private String gameName;

    /** 比赛开始时间 */
    private LocalDateTime startTime;

    /** 比赛结束时间 */
    private LocalDateTime endTime;

    /** 报名截止时间 */
    private LocalDateTime registrationDeadline;

    /** 报名战队数量上限 */
    private Integer maxTeams;

    /** 每队人数要求 */
    private Integer teamSize;

    /** 状态：0 未开始，1 报名中，2 进行中，3 已结束 */
    private Integer status;

    /** 赛事描述 */
    private String description;

    private LocalDateTime createTime;
}
