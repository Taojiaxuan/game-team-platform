package com.gameteam.vo;

import com.gameteam.entity.Registration;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报名视图对象：附带赛事名称、战队名称、游戏项目等信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationVO extends Registration {

    private String tournamentName;
    private String teamName;
    private String gameName;
}
