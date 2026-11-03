package com.gameteam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 队员
 */
@Data
@TableName("member")
public class Member {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属战队 ID */
    private Long teamId;

    /** 姓名 */
    private String name;

    /** 游戏昵称 / 游戏 ID */
    private String gameId;

    /** 队内角色：队长 / 副队长 / 队员 */
    private String role;

    /** 联系方式 */
    private String phone;

    private LocalDateTime createTime;
}
