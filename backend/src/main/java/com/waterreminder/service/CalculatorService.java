package com.waterreminder.service;

import com.waterreminder.dto.StatsDTO;
import com.waterreminder.entity.h2.DrinkRecord;
import com.waterreminder.entity.mysql.UserInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CalculatorService {

    /**
     * 计算每日需水量 (ml)
     */
    public int calculateDailyWaterNeed(UserInfo settings) {
        return settings.getWeight() * settings.getCoefficient();
    }

    /**
     * 判断当前模式: false=工作模式, true=日常模式
     */
    public String determineMode(UserInfo settings) {
        LocalTime now = LocalTime.now();
        LocalTime workStart = LocalTime.parse(settings.getWorkStart());
        LocalTime workEnd = LocalTime.parse(settings.getWorkEnd());

        if (!now.isBefore(workStart) && now.isBefore(workEnd)) {
            return "work";
        }
        return "daily";
    }

    /**
     * 计算建议每次饮水量
     */
    public int calculateSuggestedAmount(UserInfo settings, List<DrinkRecord> todayRecords) {
        int dailyNeed = calculateDailyWaterNeed(settings);
        int totalDrank = todayRecords.stream().mapToInt(DrinkRecord::getAmount).sum();
        int remaining = Math.max(0, dailyNeed - totalDrank);

        if (remaining <= 0) return 0;

        String mode = determineMode(settings);

        if ("work".equals(mode)) {
            // 工作模式：剩余水量 / 剩余30分钟次数
            LocalTime now = LocalTime.now();
            LocalTime workEnd = LocalTime.parse(settings.getWorkEnd());
            int remainingMinutes = workEnd.toSecondOfDay() / 60 - now.toSecondOfDay() / 60;
            if (remainingMinutes <= 0) remainingMinutes = 30;
            int intervals = Math.max(1, (int) Math.ceil(remainingMinutes / 30.0));
            return Math.round((float) remaining / intervals);
        } else {
            // 日常模式：剩余水量 / 距睡觉小时数
            LocalTime now = LocalTime.now();
            LocalTime sleepTime = LocalTime.parse(settings.getSleepTime());
            int remainingMinutes = sleepTime.toSecondOfDay() / 60 - now.toSecondOfDay() / 60;
            if (remainingMinutes <= 0) return remaining;
            int hours = Math.max(1, (int) Math.ceil(remainingMinutes / 60.0));
            return Math.round((float) remaining / hours);
        }
    }

    /**
     * 计算提醒倒计时秒数
     */
    public int calculateCountdownSeconds(UserInfo settings, List<DrinkRecord> todayRecords) {
        int dailyNeed = calculateDailyWaterNeed(settings);
        int totalDrank = todayRecords.stream().mapToInt(DrinkRecord::getAmount).sum();

        if (totalDrank >= dailyNeed) return 0;

        String mode = determineMode(settings);
        if ("work".equals(mode)) {
            return 30 * 60; // 30 分钟
        } else {
            // 日常模式：根据剩余量判断，如果剩余多则30分钟，否则60分钟
            int remaining = dailyNeed - totalDrank;
            int remainingMinutes;
            LocalTime now = LocalTime.now();
            LocalTime sleepTime = LocalTime.parse(settings.getSleepTime());
            remainingMinutes = sleepTime.toSecondOfDay() / 60 - now.toSecondOfDay() / 60;
            if (remainingMinutes <= 0) return 60 * 60;

            int hours = Math.max(1, (int) Math.ceil(remainingMinutes / 60.0));
            int perHour = remaining / hours;
            return perHour > 400 ? 30 * 60 : 60 * 60;
        }
    }

    /**
     * 汇总统计数据
     */
    public StatsDTO getStats(UserInfo settings, List<DrinkRecord> todayRecords) {
        int dailyNeed = calculateDailyWaterNeed(settings);
        int totalDrank = todayRecords.stream().mapToInt(DrinkRecord::getAmount).sum();
        int remaining = Math.max(0, dailyNeed - totalDrank);
        int progress = dailyNeed > 0 ? Math.min(100, Math.round((float) totalDrank / dailyNeed * 100)) : 0;
        int suggested = calculateSuggestedAmount(settings, todayRecords);
        String mode = determineMode(settings);
        int countdown = calculateCountdownSeconds(settings, todayRecords);

        return StatsDTO.builder()
                .drank(totalDrank)
                .target(dailyNeed)
                .remaining(remaining)
                .progress(progress)
                .suggestedAmount(suggested)
                .mode(mode)
                .countdownSeconds(countdown)
                .build();
    }
}
