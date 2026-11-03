package com.gameteam.controller;

import com.gameteam.common.Result;
import com.gameteam.entity.Registration;
import com.gameteam.service.RegistrationService;
import com.gameteam.vo.RegistrationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/list")
    public Result<List<RegistrationVO>> list() {
        return Result.success(registrationService.list());
    }

    @PostMapping("/register")
    public Result<Registration> register(@RequestParam Long tournamentId, @RequestParam Long teamId) {
        return Result.success("报名成功", registrationService.register(tournamentId, teamId));
    }

    @PutMapping("/review/{id}")
    public Result<Registration> review(@PathVariable Long id, @RequestParam Integer status) {
        return Result.success("审核完成", registrationService.review(id, status));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        registrationService.delete(id);
        return Result.success();
    }
}
