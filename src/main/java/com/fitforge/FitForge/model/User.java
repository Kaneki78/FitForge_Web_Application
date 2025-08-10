package com.fitforge.FitForge.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String username;
    private String email;
    private String password;
    private String role;

    private Integer age;
    private Double heightInCm;
    private Double weightInKg;
    private String gender;
    private String profilePictureFilename;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyLog> dailyLogs;

    // Methods from UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Your existing transient methods
    @Transient
    public String getBmi() {
        if (heightInCm == null || heightInCm <= 0 || weightInKg == null || weightInKg <= 0) { return "N/A"; }
        double heightInMeters = heightInCm / 100.0;
        double bmi = weightInKg / (heightInMeters * heightInMeters);
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(bmi);
    }
    @Transient
    private Double calculateBmiValue() {
        if (heightInCm == null || heightInCm <= 0 || weightInKg == null || weightInKg <= 0) { return null; }
        double heightInMeters = heightInCm / 100.0;
        return weightInKg / (heightInMeters * heightInMeters);
    }
    @Transient
    public String getBmiCategory() {
        Double bmi = calculateBmiValue();
        if (bmi == null) return "Update Profile";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Normal";
        if (bmi < 30) return "Overweight";
        return "Obese";
    }
    @Transient
    public double getBmiValueAsRotation() {
        Double bmi = calculateBmiValue();
        if (bmi == null) return 0;
        double scaledBmi = Math.max(15, Math.min(35, bmi));
        return (scaledBmi - 15) / (35 - 15) * 180.0;
    }

    // Your existing getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getHeightInCm() { return heightInCm; }
    public void setHeightInCm(Double heightInCm) { this.heightInCm = heightInCm; }
    public Double getWeightInKg() { return weightInKg; }
    public void setWeightInKg(Double weightInKg) { this.weightInKg = weightInKg; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getProfilePictureFilename() { return profilePictureFilename; }
    public void setProfilePictureFilename(String profilePictureFilename) { this.profilePictureFilename = profilePictureFilename; }
    public List<DailyLog> getDailyLogs() { return dailyLogs; }
    public void setDailyLogs(List<DailyLog> dailyLogs) { this.dailyLogs = dailyLogs; }
}