package com.gameteam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报名记录：战队报名参加某赛事
 */
@Data
@TableName("registration")
public class Registration {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 赛事 ID */
    private Long tournamentId;

    /** 战队 ID */
    private Long teamId;

    /** 报名状态：0 待审核，1 已通过，2 已拒绝 */
    private Integer status;

    private LocalDateTime registerTime;
}
