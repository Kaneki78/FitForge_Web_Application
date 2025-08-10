package com.fitforge.FitForge.service;

import com.fitforge.FitForge.dto.ChartDataDto;
import com.fitforge.FitForge.dto.DailyLogDto;
import com.fitforge.FitForge.model.DailyLog;
import com.fitforge.FitForge.model.User;
import com.fitforge.FitForge.repository.DailyLogRepository;
import com.fitforge.FitForge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final UserRepository userRepository;

    public DailyLogService(DailyLogRepository dailyLogRepository, UserRepository userRepository) {
        this.dailyLogRepository = dailyLogRepository;
        this.userRepository = userRepository;
    }

    public DailyLog findOrCreateDailyLog(User user) {
        LocalDate today = LocalDate.now();
        return dailyLogRepository.findByUserAndDate(user, today)
                .orElseGet(() -> {
                    DailyLog newLog = new DailyLog();
                    newLog.setUser(user);
                    if (user.getWeightInKg() != null) {
                        newLog.setWeightInKg(user.getWeightInKg());
                    }
                    return dailyLogRepository.save(newLog);
                });
    }

    @Transactional
    public void updateDailyStats(User user, DailyLogDto dailyLogDto) {
        DailyLog log = findOrCreateDailyLog(user);

        if (dailyLogDto.getWaterIntake() != null) log.setWaterIntake(dailyLogDto.getWaterIntake());
        if (dailyLogDto.getCaloriesConsumed() != null) log.setCaloriesConsumed(dailyLogDto.getCaloriesConsumed());
        if (dailyLogDto.getScreenTimeInMinutes() != null) log.setScreenTimeInMinutes(dailyLogDto.getScreenTimeInMinutes());
        if (dailyLogDto.getSleepTime() != null) log.setSleepTime(dailyLogDto.getSleepTime());
        if (dailyLogDto.getWakeUpTime() != null) log.setWakeUpTime(dailyLogDto.getWakeUpTime());

        if (dailyLogDto.getWeightInKg() != null && dailyLogDto.getWeightInKg() > 0) {
            log.setWeightInKg(dailyLogDto.getWeightInKg());
            user.setWeightInKg(dailyLogDto.getWeightInKg());
            userRepository.save(user);
        }

        dailyLogRepository.save(log);
    }

    public ChartDataDto getWeightChartData(User user) {
        List<DailyLog> logs = dailyLogRepository.findByUserOrderByDateAsc(user);
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");
        for (DailyLog log : logs) {
            if (log.getWeightInKg() != null && log.getWeightInKg() > 0) {
                labels.add(log.getDate().format(formatter));
                data.add(log.getWeightInKg());
            }
        }
        return new ChartDataDto(labels, data);
    }

    public ChartDataDto getSleepChartData(User user) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<DailyLog> logs = dailyLogRepository.findByUserAndDateBetweenOrderByDateAsc(user, startDate, today);
        Map<LocalDate, DailyLog> logsByDate = logs.stream()
                .collect(Collectors.toMap(DailyLog::getDate, log -> log));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            labels.add(date.format(formatter));

            DailyLog log = logsByDate.get(date);
            if (log != null && log.getSleepTime() != null && log.getWakeUpTime() != null) {
                Duration duration = Duration.between(log.getSleepTime(), log.getWakeUpTime());
                if (duration.isNegative()) {
                    duration = duration.plusDays(1);
                }
                data.add(duration.toMinutes() / 60.0);
            } else {
                data.add(0.0);
            }
        }
        return new ChartDataDto(labels, data);
    }
}