package com.gameteam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameteam.common.BusinessException;
import com.gameteam.entity.Member;
import com.gameteam.entity.Registration;
import com.gameteam.entity.Team;
import com.gameteam.entity.Tournament;
import com.gameteam.mapper.MemberMapper;
import com.gameteam.mapper.RegistrationMapper;
import com.gameteam.mapper.TeamMapper;
import com.gameteam.mapper.TournamentMapper;
import com.gameteam.vo.RegistrationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationMapper registrationMapper;
    private final TournamentMapper tournamentMapper;
    private final TeamMapper teamMapper;
    private final MemberMapper memberMapper;

    /** 报名列表，附带赛事与战队信息 */
    public List<RegistrationVO> list() {
        List<Registration> registrations = registrationMapper.selectList(
                new LambdaQueryWrapper<Registration>().orderByDesc(Registration::getRegisterTime));

        Map<Long, Tournament> tournamentMap = tournamentMapper.selectList(null).stream()
                .collect(Collectors.toMap(Tournament::getId, Function.identity()));
        Map<Long, Team> teamMap = teamMapper.selectList(null).stream()
                .collect(Collectors.toMap(Team::getId, Function.identity()));

        return registrations.stream().map(r -> {
            RegistrationVO vo = new RegistrationVO();
            vo.setId(r.getId());
            vo.setTournamentId(r.getTournamentId());
            vo.setTeamId(r.getTeamId());
            vo.setStatus(r.getStatus());
            vo.setRegisterTime(r.getRegisterTime());
            Tournament t = tournamentMap.get(r.getTournamentId());
            Team team = teamMap.get(r.getTeamId());
            if (t != null) {
                vo.setTournamentName(t.getName());
                vo.setGameName(t.getGameName());
            }
            if (team != null) {
                vo.setTeamName(team.getName());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 战队报名参加赛事。
     * 校验规则：
     * 1. 赛事存在且处于「报名中」状态；
     * 2. 未过报名截止时间；
     * 3. 战队存在；
     * 4. 战队队员人数满足赛事要求；
     * 5. 已报名（未拒绝）的战队数量未达上限；
     * 6. 同一战队不能重复报名同一赛事。
     */
    @Transactional
    public Registration register(Long tournamentId, Long teamId) {
        Tournament tournament = tournamentMapper.selectById(tournamentId);
        if (tournament == null) {
            throw new BusinessException("赛事不存在");
        }
        if (tournament.getStatus() == null || tournament.getStatus() != 1) {
            throw new BusinessException("该赛事当前不在报名阶段");
        }
        if (tournament.getRegistrationDeadline() != null
                && LocalDateTime.now().isAfter(tournament.getRegistrationDeadline())) {
            throw new BusinessException("报名已截止");
        }

        Team team = teamMapper.selectById(teamId);
        if (team == null) {
            throw new BusinessException("战队不存在");
        }

        int memberCount = memberMapper.selectCount(
                new LambdaQueryWrapper<Member>().eq(Member::getTeamId, teamId)).intValue();
        if (memberCount < tournament.getTeamSize()) {
            throw new BusinessException("队员人数不足，该赛事要求每队至少 " + tournament.getTeamSize() + " 人，当前 " + memberCount + " 人");
        }

        long registered = registrationMapper.selectCount(
                new LambdaQueryWrapper<Registration>()
                        .eq(Registration::getTournamentId, tournamentId)
                        .ne(Registration::getStatus, 2));
        if (registered >= tournament.getMaxTeams()) {
            throw new BusinessException("报名战队数量已达上限");
        }

        Long existed = registrationMapper.selectCount(
                new LambdaQueryWrapper<Registration>()
                        .eq(Registration::getTournamentId, tournamentId)
                        .eq(Registration::getTeamId, teamId));
        if (existed > 0) {
            throw new BusinessException("该战队已报名此赛事，请勿重复报名");
        }

        Registration registration = new Registration();
        registration.setTournamentId(tournamentId);
        registration.setTeamId(teamId);
        registration.setStatus(0);
        registration.setRegisterTime(LocalDateTime.now());
        registrationMapper.insert(registration);
        return registration;
    }

    /** 审核报名：通过(1) / 拒绝(2) */
    @Transactional
    public Registration review(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new BusinessException("审核状态只能为 1(通过) 或 2(拒绝)");
        }
        Registration registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        registration.setStatus(status);
        registrationMapper.updateById(registration);
        return registration;
    }

    @Transactional
    public void delete(Long id) {
        if (registrationMapper.selectById(id) == null) {
            throw new BusinessException("报名记录不存在");
        }
        registrationMapper.deleteById(id);
    }
}
