// PATH: src/main/java/com/fitforge/FitForge/service/GoalService.java

package com.fitforge.FitForge.service;

import com.fitforge.FitForge.dto.GoalDto;
import com.fitforge.FitForge.model.Goal;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public List<Goal> findGoalsByUser(User user) {
        return goalRepository.findByUser(user);
    }

    public List<Goal> findActiveGoalsByUser(User user) {
        return goalRepository.findByUserAndActive(user, true);
    }

    public void createGoal(User user, GoalDto goalDto) {
        Goal goal = new Goal();
        goal.setUser(user);
        goal.setGoalType(goalDto.getGoalType());
        goal.setTargetValue(goalDto.getTargetValue());
        goal.setStartDate(LocalDate.now());
        goal.setEndDate(goalDto.getEndDate());
        goal.setActive(true);

        // Set the start value based on the goal type
        if ("TARGET_WEIGHT".equals(goalDto.getGoalType())) {
            goal.setStartValue(user.getWeightInKg() != null ? user.getWeightInKg() : 0);
            goal.setCurrentValue(user.getWeightInKg() != null ? user.getWeightInKg() : 0);
        } else {
            goal.setStartValue(0);
            goal.setCurrentValue(0);
        }

        goalRepository.save(goal);
    }
}