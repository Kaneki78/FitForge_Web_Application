package com.fitforge.FitForge.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserProfileDto {

    @NotNull(message = "Age is required")
    @Min(value = 13, message = "You must be at least 13 years old")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

    @NotNull(message = "Height is required")
    @Min(value = 50, message = "Height must be in centimeters")
    private Double heightInCm;

    @NotEmpty(message = "Please select a gender")
    private String gender;

    // Getters and Setters
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getHeightInCm() { return heightInCm; }
    public void setHeightInCm(Double heightInCm) { this.heightInCm = heightInCm; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}