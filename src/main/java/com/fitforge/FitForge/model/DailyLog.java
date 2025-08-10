package com.fitforge.FitForge.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate date;
    private Integer waterIntake;
    private Integer caloriesConsumed;
    private Double weightInKg;
    private Integer screenTimeInMinutes;

    private LocalTime sleepTime; // <-- NEW FIELD
    private LocalTime wakeUpTime; // <-- NEW FIELD

    public DailyLog() {
        this.date = LocalDate.now();
        this.waterIntake = 0;
        this.caloriesConsumed = 0;
        this.screenTimeInMinutes = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
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