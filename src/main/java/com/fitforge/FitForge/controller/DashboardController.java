package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.dto.DailyLogDto;
import com.fitforge.FitForge.dto.WorkoutDto;
import com.fitforge.FitForge.model.DailyLog;
import com.fitforge.FitForge.model.Goal;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final DailyLogService dailyLogService;
    private final GoalService goalService;
    private final GoalUpdateService goalUpdateService;
    private final WorkoutService workoutService;
    private final HealthService healthService;

    public DashboardController(UserService userService, DailyLogService dailyLogService, GoalService goalService, GoalUpdateService goalUpdateService, WorkoutService workoutService, HealthService healthService) {
        this.userService = userService;
        this.dailyLogService = dailyLogService;
        this.goalService = goalService;
        this.goalUpdateService = goalUpdateService;
        this.workoutService = workoutService;
        this.healthService = healthService;
    }

    @GetMapping("/dashboard")
    public String showUserDashboard(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        goalUpdateService.updateAllActiveGoals(user);

        DailyLog todayLog = dailyLogService.findOrCreateDailyLog(user);
        List<Goal> activeGoals = goalService.findActiveGoalsByUser(user);
        int waterGoal = healthService.calculateWaterIntakeGoal(user);

        // Screen Time Tip Logic
        String screenTimeTip;
        int screenTime = todayLog.getScreenTimeInMinutes() != null ? todayLog.getScreenTimeInMinutes() : 0;
        if (screenTime > 240) screenTimeTip = "High screen time. Remember to use the 20-20-20 rule to protect your eyes!";
        else if (screenTime > 120) screenTimeTip = "Screen time is moderate. Consider a short walk to stretch your legs.";
        else screenTimeTip = "Low screen time, that's fantastic for your well-being! Keep up the great work.";

        // Sleep Tip Logic
        String sleepTip;
        if (todayLog.getSleepTime() != null && todayLog.getWakeUpTime() != null) {
            Duration sleepDuration = Duration.between(todayLog.getSleepTime(), todayLog.getWakeUpTime());
            if (sleepDuration.isNegative()) sleepDuration = sleepDuration.plusDays(1);
            long hours = sleepDuration.toHours();
            if (hours < 6) sleepTip = "Less than 6 hours of sleep can impact focus. Aim for an earlier bedtime tonight.";
            else if (hours > 9) sleepTip = "More than 9 hours of sleep. While rest is good, consistency is key for a healthy sleep cycle.";
            else sleepTip = "Great sleep duration! Consistent, quality sleep is a pillar of good health.";
        } else {
            sleepTip = "Log your sleep and wake-up times to get personalized feedback on your sleep habits.";
        }

        model.addAttribute("screenTimeTip", screenTimeTip);
        model.addAttribute("sleepTip", sleepTip);
        model.addAttribute("waterGoal", waterGoal);
        model.addAttribute("user", user);
        model.addAttribute("todayLog", todayLog);
        model.addAttribute("activeGoals", activeGoals);
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("workoutDto", new WorkoutDto());

        DailyLogDto dailyLogDto = new DailyLogDto();
        dailyLogDto.setWaterIntake(todayLog.getWaterIntake());
        dailyLogDto.setCaloriesConsumed(todayLog.getCaloriesConsumed());
        dailyLogDto.setScreenTimeInMinutes(todayLog.getScreenTimeInMinutes());
        dailyLogDto.setSleepTime(todayLog.getSleepTime());
        dailyLogDto.setWakeUpTime(todayLog.getWakeUpTime());
        model.addAttribute("dailyLogDto", dailyLogDto);

        return "user-dashboard";
    }

    @PostMapping("/dashboard/log")
    public String updateDailyStats(@ModelAttribute("dailyLogDto") DailyLogDto dailyLogDto, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        dailyLogService.updateDailyStats(user, dailyLogDto);
        goalUpdateService.updateAllActiveGoals(user);
        return "redirect:/dashboard";
    }
}