// PATH: src/main/java/com/fitforge/FitForge/service/GoalUpdateService.java

package com.fitforge.FitForge.service;

import com.fitforge.FitForge.model.Goal;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.repository.GoalRepository;
import com.fitforge.FitForge.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class GoalUpdateService {

    private final GoalRepository goalRepository;
    private final WorkoutRepository workoutRepository;

    public GoalUpdateService(GoalRepository goalRepository, WorkoutRepository workoutRepository) {
        this.goalRepository = goalRepository;
        this.workoutRepository = workoutRepository;
    }

    @Transactional
    public void updateAllActiveGoals(User user) {
        // Fetch goals using the correct method findByUserAndActive
        List<Goal> activeGoals = goalRepository.findByUserAndActive(user, true);

        for (Goal goal : activeGoals) {
            // Check if the goal has expired
            if (goal.getEndDate().isBefore(LocalDate.now())) {
                goal.setActive(false); // Set active to false, not a string status
                goalRepository.save(goal);
                continue; // Move to the next goal
            }

            // Update progress based on goal type
            if ("WEEKLY_WORKOUTS".equals(goal.getGoalType())) {
                updateWeeklyWorkoutGoal(user, goal);
            } else if ("TARGET_WEIGHT".equals(goal.getGoalType())) {
                updateTargetWeightGoal(user, goal);
            }
        }
    }

    private void updateWeeklyWorkoutGoal(User user, Goal goal) {
        // Find the start of the current week (Monday)
        LocalDateTime startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        // Count workouts since the start of the week using our new repository method
        int workoutsThisWeek = workoutRepository.countWorkoutsSince(user, startOfWeek);
        goal.setCurrentValue(workoutsThisWeek);

        // Check if the goal is met
        if (goal.getCurrentValue() >= goal.getTargetValue()) {
            goal.setActive(false); // Mark as complete
        }
        goalRepository.save(goal);
    }

    private void updateTargetWeightGoal(User user, Goal goal) {
        if (user.getWeightInKg() != null) {
            goal.setCurrentValue(user.getWeightInKg());

            // Check if the user has reached their target weight
            // This works whether they are losing or gaining weight
            if ((goal.getStartValue() > goal.getTargetValue() && user.getWeightInKg() <= goal.getTargetValue()) ||
                    (goal.getStartValue() < goal.getTargetValue() && user.getWeightInKg() >= goal.getTargetValue())) {
                goal.setActive(false); // Mark as complete
            }
            goalRepository.save(goal);
        }
    }
}