package com.fitforge.FitForge.repository;

import com.fitforge.FitForge.dto.UserRankingDto;
import com.fitforge.FitForge.dto.WorkoutSummaryDto;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByUserOrderByStartTimeDesc(User user);

    @Query("SELECT new com.fitforge.FitForge.dto.WorkoutSummaryDto(COALESCE(SUM(w.durationInMinutes), 0L), COALESCE(SUM(w.caloriesBurned), 0L)) " +
            "FROM Workout w " +
            "WHERE w.user = :user AND w.startTime >= :startOfDay AND w.startTime < :endOfDay")
    WorkoutSummaryDto getWorkoutSummaryForDate(@Param("user") User user, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user = :user AND w.startTime >= :since")
    int countWorkoutsSince(@Param("user") User user, @Param("since") LocalDateTime since);

    @Query("SELECT new com.fitforge.FitForge.dto.UserRankingDto(w.user.username, w.user.profilePictureFilename, COUNT(w)) " +
            "FROM Workout w " +
            "WHERE w.startTime >= :startDate AND w.startTime < :endDate " +
            "GROUP BY w.user.id, w.user.username, w.user.profilePictureFilename " +
            "ORDER BY COUNT(w) DESC")
    List<UserRankingDto> findUserRankingsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}