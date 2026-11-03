package com.gameteam.controller;

import com.gameteam.common.Result;
import com.gameteam.entity.Tournament;
import com.gameteam.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournament")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping("/list")
    public Result<List<Tournament>> list(@RequestParam(required = false) Integer status) {
        return Result.success(tournamentService.list(status));
    }

    @GetMapping("/{id}")
    public Result<Tournament> get(@PathVariable Long id) {
        return Result.success(tournamentService.get(id));
    }

    @PostMapping
    public Result<Tournament> create(@RequestBody Tournament tournament) {
        return Result.success("创建成功", tournamentService.create(tournament));
    }

    @PutMapping
    public Result<Tournament> update(@RequestBody Tournament tournament) {
        return Result.success("更新成功", tournamentService.update(tournament));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tournamentService.delete(id);
        return Result.success();
    }
}
