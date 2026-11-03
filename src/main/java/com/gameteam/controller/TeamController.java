package com.gameteam.controller;

import com.gameteam.common.Result;
import com.gameteam.entity.Team;
import com.gameteam.service.TeamService;
import com.gameteam.vo.TeamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/list")
    public Result<List<TeamVO>> list() {
        return Result.success(teamService.list());
    }

    @GetMapping("/{id}")
    public Result<TeamVO> get(@PathVariable Long id) {
        return Result.success(teamService.get(id));
    }

    @PostMapping
    public Result<Team> create(@RequestBody Team team) {
        return Result.success("创建成功", teamService.create(team));
    }

    @PutMapping
    public Result<Team> update(@RequestBody Team team) {
        return Result.success("更新成功", teamService.update(team));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return Result.success();
    }
}
