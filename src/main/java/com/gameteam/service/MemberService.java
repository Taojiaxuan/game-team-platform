package com.gameteam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameteam.common.BusinessException;
import com.gameteam.entity.Member;
import com.gameteam.entity.Team;
import com.gameteam.mapper.MemberMapper;
import com.gameteam.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final TeamMapper teamMapper;

    /** 队员列表，可按战队筛选 */
    public List<Member> list(Long teamId) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<Member>()
                .orderByDesc(Member::getCreateTime);
        if (teamId != null) {
            wrapper.eq(Member::getTeamId, teamId);
        }
        return memberMapper.selectList(wrapper);
    }

    public Member get(Long id) {
        Member member = memberMapper.selectById(id);
        if (member == null) {
            throw new BusinessException("队员不存在");
        }
        return member;
    }

    @Transactional
    public Member create(Member member) {
        if (member.getName() == null || member.getName().isBlank()) {
            throw new BusinessException("姓名不能为空");
        }
        if (member.getTeamId() == null || teamMapper.selectById(member.getTeamId()) == null) {
            throw new BusinessException("所属战队不存在");
        }
        member.setId(null);
        member.setCreateTime(LocalDateTime.now());
        memberMapper.insert(member);
        return member;
    }

    @Transactional
    public Member update(Member member) {
        if (member.getId() == null || memberMapper.selectById(member.getId()) == null) {
            throw new BusinessException("队员不存在");
        }
        if (member.getTeamId() != null && teamMapper.selectById(member.getTeamId()) == null) {
            throw new BusinessException("所属战队不存在");
        }
        memberMapper.updateById(member);
        return memberMapper.selectById(member.getId());
    }

    @Transactional
    public void delete(Long id) {
        if (memberMapper.selectById(id) == null) {
            throw new BusinessException("队员不存在");
        }
        memberMapper.deleteById(id);
    }

    /** 统计某战队队员数量，供报名校验使用 */
    public int countByTeam(Long teamId) {
        return memberMapper.selectCount(
                new LambdaQueryWrapper<Member>().eq(Member::getTeamId, teamId)).intValue();
    }
}
