// PATH: src/main/java/com/fitforge/FitForge/repository/GoalRepository.java

package com.fitforge.FitForge.repository;

import com.fitforge.FitForge.model.Goal;
import com.fitforge.FitForge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(User user);
    List<Goal> findByUserAndActive(User user, boolean active);
}