package com.gameteam.controller;

import com.gameteam.common.Result;
import com.gameteam.entity.Member;
import com.gameteam.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public Result<List<Member>> list(@RequestParam(required = false) Long teamId) {
        return Result.success(memberService.list(teamId));
    }

    @GetMapping("/{id}")
    public Result<Member> get(@PathVariable Long id) {
        return Result.success(memberService.get(id));
    }

    @PostMapping
    public Result<Member> create(@RequestBody Member member) {
        return Result.success("创建成功", memberService.create(member));
    }

    @PutMapping
    public Result<Member> update(@RequestBody Member member) {
        return Result.success("更新成功", memberService.update(member));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return Result.success();
    }
}
