package com.gameteam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameteam.common.BusinessException;
import com.gameteam.entity.Registration;
import com.gameteam.entity.Tournament;
import com.gameteam.mapper.RegistrationMapper;
import com.gameteam.mapper.TournamentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentMapper tournamentMapper;
    private final RegistrationMapper registrationMapper;

    /** 赛事列表，可按状态筛选 */
    public List<Tournament> list(Integer status) {
        LambdaQueryWrapper<Tournament> wrapper = new LambdaQueryWrapper<Tournament>()
                .orderByDesc(Tournament::getCreateTime);
        if (status != null) {
            wrapper.eq(Tournament::getStatus, status);
        }
        return tournamentMapper.selectList(wrapper);
    }

    public Tournament get(Long id) {
        Tournament tournament = tournamentMapper.selectById(id);
        if (tournament == null) {
            throw new BusinessException("赛事不存在");
        }
        return tournament;
    }

    @Transactional
    public Tournament create(Tournament tournament) {
        validate(tournament);
        tournament.setId(null);
        tournament.setCreateTime(LocalDateTime.now());
        tournamentMapper.insert(tournament);
        return tournament;
    }

    @Transactional
    public Tournament update(Tournament tournament) {
        if (tournament.getId() == null || tournamentMapper.selectById(tournament.getId()) == null) {
            throw new BusinessException("赛事不存在");
        }
        validate(tournament);
        tournamentMapper.updateById(tournament);
        return tournamentMapper.selectById(tournament.getId());
    }

    /** 删除赛事，同时删除其报名记录 */
    @Transactional
    public void delete(Long id) {
        if (tournamentMapper.selectById(id) == null) {
            throw new BusinessException("赛事不存在");
        }
        registrationMapper.delete(new LambdaQueryWrapper<Registration>().eq(Registration::getTournamentId, id));
        tournamentMapper.deleteById(id);
    }

    private void validate(Tournament t) {
        if (t.getName() == null || t.getName().isBlank()) {
            throw new BusinessException("赛事名称不能为空");
        }
        if (t.getGameName() == null || t.getGameName().isBlank()) {
            throw new BusinessException("游戏项目不能为空");
        }
        if (t.getStartTime() != null && t.getEndTime() != null && t.getEndTime().isBefore(t.getStartTime())) {
            throw new BusinessException("结束时间不能早于开始时间");
        }
        if (t.getMaxTeams() == null || t.getMaxTeams() <= 0) {
            throw new BusinessException("报名战队上限必须大于 0");
        }
        if (t.getTeamSize() == null || t.getTeamSize() <= 0) {
            throw new BusinessException("每队人数要求必须大于 0");
        }
    }
}
