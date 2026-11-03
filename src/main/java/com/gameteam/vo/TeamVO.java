package com.gameteam.vo;

import com.gameteam.entity.Team;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 战队视图对象：在战队基础上附带队员数量
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamVO extends Team {

    /** 队员数量 */
    private Integer memberCount;
}
