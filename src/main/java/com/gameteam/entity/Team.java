package com.gameteam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 战队
 */
@Data
@TableName("team")
public class Team {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 战队名称 */
    private String name;

    /** 战队标签 / 口号 */
    private String tag;

    /** 队长姓名 */
    private String captainName;

    /** 联系方式 */
    private String contactPhone;

    /** 战队介绍 */
    private String description;

    private LocalDateTime createTime;
}
