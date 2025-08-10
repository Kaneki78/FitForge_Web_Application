// PATH: src/main/java/com/fitforge/FitForge/dto/WorkoutDto.java

package com.fitforge.FitForge.dto;

public class WorkoutDto {

    private String workoutType;
    private Integer durationInMinutes;
    private Integer caloriesBurned;

    // Getters and Setters
    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }
}