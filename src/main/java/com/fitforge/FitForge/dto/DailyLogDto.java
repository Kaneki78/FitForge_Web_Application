package com.fitforge.FitForge.dto;

import java.time.LocalTime;

public class DailyLogDto {

    private Integer waterIntake;
    private Integer caloriesConsumed;
    private Double weightInKg;
    private Integer screenTimeInMinutes;
    private LocalTime sleepTime; // <-- NEW FIELD
    private LocalTime wakeUpTime; // <-- NEW FIELD

    // Getters and Setters
    public Integer getWaterIntake() { return waterIntake; }
    public void setWaterIntake(Integer waterIntake) { this.waterIntake = waterIntake; }
    public Integer getCaloriesConsumed() { return caloriesConsumed; }
    public void setCaloriesConsumed(Integer caloriesConsumed) { this.caloriesConsumed = caloriesConsumed; }
    public Double getWeightInKg() { return weightInKg; }
    public void setWeightInKg(Double weightInKg) { this.weightInKg = weightInKg; }
    public Integer getScreenTimeInMinutes() { return screenTimeInMinutes; }
    public void setScreenTimeInMinutes(Integer screenTimeInMinutes) { this.screenTimeInMinutes = screenTimeInMinutes; }

    // Getters and Setters for new fields
    public LocalTime getSleepTime() { return sleepTime; }
    public void setSleepTime(LocalTime sleepTime) { this.sleepTime = sleepTime; }
    public LocalTime getWakeUpTime() { return wakeUpTime; }
    public void setWakeUpTime(LocalTime wakeUpTime) { this.wakeUpTime = wakeUpTime; }
}