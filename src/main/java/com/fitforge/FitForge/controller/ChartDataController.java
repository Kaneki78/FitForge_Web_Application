package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.dto.ChartDataDto;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.service.DailyLogService;
import com.fitforge.FitForge.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChartDataController {

    private final DailyLogService dailyLogService;
    private final UserService userService;

    public ChartDataController(DailyLogService dailyLogService, UserService userService) {
        this.dailyLogService = dailyLogService;
        this.userService = userService;
    }

    @GetMapping("/weight-progress")
    public ResponseEntity<ChartDataDto> getWeightProgressChart(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(dailyLogService.getWeightChartData(user));
    }

    @GetMapping("/sleep-progress")
    public ResponseEntity<ChartDataDto> getSleepProgressChart(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(dailyLogService.getSleepChartData(user));
    }
}