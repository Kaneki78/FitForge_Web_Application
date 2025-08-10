// PATH: src/main/java/com/fitforge/FitForge/dto/WorkoutSummaryDto.java

package com.fitforge.FitForge.dto;

public class WorkoutSummaryDto {

    private long totalDurationInMinutes;
    private long totalCaloriesBurned;

    // A no-args constructor is good practice
    public WorkoutSummaryDto() {
        this.totalDurationInMinutes = 0;
        this.totalCaloriesBurned = 0;
    }

    // THIS IS THE CONSTRUCTOR THAT WAS MISSING.
    // Spring Data JPA will use this to create the object from the query result.
    public WorkoutSummaryDto(long totalDurationInMinutes, long totalCaloriesBurned) {
        this.totalDurationInMinutes = totalDurationInMinutes;
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    // Getters and Setters
    public long getTotalDurationInMinutes() {
        return totalDurationInMinutes;
    }

    public void setTotalDurationInMinutes(long totalDurationInMinutes) {
        this.totalDurationInMinutes = totalDurationInMinutes;
    }

    public long getTotalCaloriesBurned() {
        return totalCaloriesBurned;
    }

    public void setTotalCaloriesBurned(long totalCaloriesBurned) {
        this.totalCaloriesBurned = totalCaloriesBurned;
    }
}