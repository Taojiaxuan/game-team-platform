package com.gameteam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameteam.common.BusinessException;
import com.gameteam.entity.Member;
import com.gameteam.entity.Registration;
import com.gameteam.entity.Team;
import com.gameteam.mapper.MemberMapper;
import com.gameteam.mapper.RegistrationMapper;
import com.gameteam.mapper.TeamMapper;
import com.gameteam.vo.TeamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;
    private final MemberMapper memberMapper;
    private final RegistrationMapper registrationMapper;

    /** 战队列表，附带队员数量 */
    public List<TeamVO> list() {
        return teamMapper.selectList(new LambdaQueryWrapper<Team>().orderByDesc(Team::getCreateTime))
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    public TeamVO get(Long id) {
        Team team = teamMapper.selectById(id);
        if (team == null) {
            throw new BusinessException("战队不存在");
        }
        return toVO(team);
    }

    @Transactional
    public Team create(Team team) {
        checkNameUnique(team.getName(), null);
        team.setId(null);
        team.setCreateTime(LocalDateTime.now());
        teamMapper.insert(team);
        return team;
    }

    @Transactional
    public Team update(Team team) {
        if (team.getId() == null || teamMapper.selectById(team.getId()) == null) {
            throw new BusinessException("战队不存在");
        }
        checkNameUnique(team.getName(), team.getId());
        teamMapper.updateById(team);
        return teamMapper.selectById(team.getId());
    }

    /** 删除战队，同时删除其队员与报名记录 */
    @Transactional
    public void delete(Long id) {
        if (teamMapper.selectById(id) == null) {
            throw new BusinessException("战队不存在");
        }
        memberMapper.delete(new LambdaQueryWrapper<Member>().eq(Member::getTeamId, id));
        registrationMapper.delete(new LambdaQueryWrapper<Registration>().eq(Registration::getTeamId, id));
        teamMapper.deleteById(id);
    }

    private void checkNameUnique(String name, Long excludeId) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("战队名称不能为空");
        }
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper<Team>()
                .eq(Team::getName, name);
        if (excludeId != null) {
            wrapper.ne(Team::getId, excludeId);
        }
        if (teamMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("战队名称已存在");
        }
    }

    private TeamVO toVO(Team team) {
        TeamVO vo = new TeamVO();
        vo.setId(team.getId());
        vo.setName(team.getName());
        vo.setTag(team.getTag());
        vo.setCaptainName(team.getCaptainName());
        vo.setContactPhone(team.getContactPhone());
        vo.setDescription(team.getDescription());
        vo.setCreateTime(team.getCreateTime());
        vo.setMemberCount(memberMapper.selectCount(
                new LambdaQueryWrapper<Member>().eq(Member::getTeamId, team.getId())).intValue());
        return vo;
    }
}
