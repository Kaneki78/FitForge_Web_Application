package com.fitforge.FitForge.service;

import com.fitforge.FitForge.model.User;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    /**
     * Calculates the recommended daily water intake in milliliters (ml).
     * This is a baseline calculation. A more advanced version could include activity level.
     * Formula: Base of 35ml per kg of weight for males, 31ml for females.
     * @param user The user for whom to calculate the goal.
     * @return The recommended daily water intake in ml. Returns a default if user data is incomplete.
     */
    public int calculateWaterIntakeGoal(User user) {
        if (user.getWeightInKg() == null || user.getGender() == null) {
            return 2500; // Return a sensible default if weight/gender isn't set.
        }

        double weight = user.getWeightInKg();
        double baseMultiplier = 35.0; // Default for Male

        if ("FEMALE".equalsIgnoreCase(user.getGender())) {
            baseMultiplier = 31.0;
        }

        // Calculate the goal and round to the nearest 50ml for a cleaner number.
        double calculatedGoal = weight * baseMultiplier;
        return (int) (Math.round(calculatedGoal / 50.0) * 50);
    }
}