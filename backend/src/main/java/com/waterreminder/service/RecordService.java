package com.waterreminder.service;

import com.waterreminder.dto.RecordDTO;
import com.waterreminder.entity.h2.DrinkRecord;
import com.waterreminder.entity.mysql.UserInfo;
import com.waterreminder.repository.h2.DrinkRecordRepository;
import com.waterreminder.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private final DrinkRecordRepository recordRepository;
    private final CalculatorService calculatorService;
    private final SettingsService settingsService;
    private final CurrentUserService currentUserService;

    public RecordService(DrinkRecordRepository recordRepository,
                         CalculatorService calculatorService,
                         SettingsService settingsService,
                         CurrentUserService currentUserService) {
        this.recordRepository = recordRepository;
        this.calculatorService = calculatorService;
        this.settingsService = settingsService;
        this.currentUserService = currentUserService;
    }

    public List<RecordDTO> getTodayRecords() {
        UserInfo user = currentUserService.getCurrentUser();
        String today = LocalDate.now().toString();
        return recordRepository.findByUserIdAndRecordDateOrderByCreatedAtDesc(user.getId(), today)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional("h2TransactionManager")
    public RecordDTO addRecord(Integer amount) {
        UserInfo user = currentUserService.getCurrentUser();
        String today = LocalDate.now().toString();
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String mode = calculatorService.determineMode(settingsService.getSettings());

        DrinkRecord record = DrinkRecord.builder()
                .amount(amount)
                .mode(mode)
                .recordTime(now)
                .recordDate(today)
                .userId(user.getId())
                .build();

        record = recordRepository.save(record);
        return toDTO(record);
    }

    @Transactional("h2TransactionManager")
    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }

    public List<DrinkRecord> getTodayRecordsRaw() {
        UserInfo user = currentUserService.getCurrentUser();
        String today = LocalDate.now().toString();
        return recordRepository.findByUserIdAndRecordDate(user.getId(), today);
    }

    private RecordDTO toDTO(DrinkRecord r) {
        return RecordDTO.builder()
                .id(r.getId())
                .amount(r.getAmount())
                .mode(r.getMode())
                .recordTime(r.getRecordTime())
                .recordDate(r.getRecordDate())
                .build();
    }
}
