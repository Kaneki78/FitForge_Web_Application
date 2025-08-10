package com.fitforge.FitForge.repository;

import com.fitforge.FitForge.model.DailyLog;
import com.fitforge.FitForge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);

    List<DailyLog> findByUserOrderByDateAsc(User user);

    List<DailyLog> findByUserAndDateBetweenOrderByDateAsc(User user, LocalDate startDate, LocalDate endDate);
}