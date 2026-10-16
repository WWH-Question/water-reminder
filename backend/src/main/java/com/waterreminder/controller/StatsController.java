package com.waterreminder.controller;

import com.waterreminder.dto.StatsDTO;
import com.waterreminder.entity.h2.DrinkRecord;
import com.waterreminder.entity.mysql.UserInfo;
import com.waterreminder.service.CalculatorService;
import com.waterreminder.service.RecordService;
import com.waterreminder.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final CalculatorService calculatorService;
    private final SettingsService settingsService;
    private final RecordService recordService;

    public StatsController(CalculatorService calculatorService,
                           SettingsService settingsService,
                           RecordService recordService) {
        this.calculatorService = calculatorService;
        this.settingsService = settingsService;
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<StatsDTO> getStats() {
        UserInfo settings = settingsService.getSettings();
        List<DrinkRecord> todayRecords = recordService.getTodayRecordsRaw();
        StatsDTO stats = calculatorService.getStats(settings, todayRecords);
        return ResponseEntity.ok(stats);
    }
}
