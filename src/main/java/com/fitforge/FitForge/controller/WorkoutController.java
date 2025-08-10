package com.fitforge.FitForge.controller;

import com.fitforge.FitForge.dto.WorkoutDto;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.model.Workout;
import com.fitforge.FitForge.service.UserService;
import com.fitforge.FitForge.service.WorkoutService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @PostMapping("/workout/add")
    public String addWorkout(@ModelAttribute("workoutDto") WorkoutDto workoutDto, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        workoutService.saveWorkout(workoutDto, user);

        return "redirect:/dashboard";
    }

    @GetMapping("/workout-history")
    public String showWorkoutHistory(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Workout> workouts = workoutService.findWorkoutsByUser(user);

        model.addAttribute("workouts", workouts);
        model.addAttribute("activePage", "workout-history");

        return "workout-history";
    }
}