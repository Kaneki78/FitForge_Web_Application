// PATH: src/main/java/com/fitforge/FitForge/service/RankingService.java

package com.fitforge.FitForge.service;

import com.fitforge.FitForge.dto.UserRankingDto;
import com.fitforge.FitForge.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RankingService {

    private final WorkoutRepository workoutRepository;

    public RankingService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<UserRankingDto> getWeeklyRankings() {
        // Define the start and end of the current week
        LocalDate today = LocalDate.now();
        LocalDateTime startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        // Call our new, working repository method
        List<UserRankingDto> rankings = workoutRepository.findUserRankingsByDateRange(startOfWeek, endOfWeek);

        // Set the rank number (1, 2, 3...) for each user in the list
        AtomicLong rank = new AtomicLong(1);
        rankings.forEach(rankingDto -> rankingDto.setRank(rank.getAndIncrement()));

        return rankings;
    }
}