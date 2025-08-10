// PATH: src/main/java/com/fitforge/FitForge/controller/GoalController.java

package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.dto.GoalDto;
import com.fitforge.FitForge.model.Goal;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.service.GoalService;
import com.fitforge.FitForge.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GoalController {

    private final GoalService goalService;
    private final UserService userService;

    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
    }

    @GetMapping("/goals")
    public String showGoalsPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Goal> allGoals = goalService.findGoalsByUser(user);

        model.addAttribute("allGoals", allGoals);
        model.addAttribute("goalDto", new GoalDto()); // For the form
        model.addAttribute("activePage", "goals"); // For the sidebar
        return "goals";
    }

    @PostMapping("/goals/add")
    public String addGoal(@ModelAttribute("goalDto") GoalDto goalDto, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        goalService.createGoal(user, goalDto);
        return "redirect:/goals";
    }
}