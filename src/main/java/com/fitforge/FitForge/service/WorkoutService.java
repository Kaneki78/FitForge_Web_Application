package com.fitforge.FitForge.service;

import com.fitforge.FitForge.dto.WorkoutDto;
import com.fitforge.FitForge.dto.WorkoutSummaryDto;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.model.Workout;
import com.fitforge.FitForge.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public void saveWorkout(WorkoutDto workoutDto, User user) {
        Workout workout = new Workout();
        workout.setUser(user);
        workout.setWorkoutType(workoutDto.getWorkoutType());
        workout.setDurationInMinutes(workoutDto.getDurationInMinutes());
        workout.setStartTime(LocalDateTime.now());

        int calculatedCalories = calculateCaloriesBurned(
                workoutDto.getWorkoutType(),
                workoutDto.getDurationInMinutes(),
                user.getWeightInKg()
        );
        workout.setCaloriesBurned(calculatedCalories);

        workoutRepository.save(workout);
    }

    private int calculateCaloriesBurned(String workoutType, Integer durationInMinutes, Double userWeightKg) {
        if (durationInMinutes == null || durationInMinutes <= 0) {
            return 0;
        }

        if (userWeightKg == null || userWeightKg <= 0) {
            userWeightKg = 70.0;
        }

        Map<String, Double> metValues = Map.of(
                "Running", 9.8,
                "Weightlifting", 5.0,
                "Cycling", 7.5,
                "Yoga", 2.5,
                "Swimming", 8.0
        );

        double metValue = metValues.getOrDefault(workoutType, 4.0);
        double calories = (double) durationInMinutes * metValue * 3.5 * userWeightKg / 200.0;

        return (int) Math.round(calories);
    }

    public List<Workout> findWorkoutsByUser(User user) {
        return workoutRepository.findByUserOrderByStartTimeDesc(user);
    }

    public WorkoutSummaryDto getTodayWorkoutSummary(User user) {
        LocalDate today = LocalDate.now();
        WorkoutSummaryDto summary = workoutRepository.getWorkoutSummaryForDate(user, today.atStartOfDay(), today.atTime(LocalTime.MAX));

        if (summary == null) {
            return new WorkoutSummaryDto(0, 0);
        }
        return summary;
    }
}