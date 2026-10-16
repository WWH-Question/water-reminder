package com.waterreminder.repository.h2;

import com.waterreminder.entity.h2.DrinkRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrinkRecordRepository extends JpaRepository<DrinkRecord, Long> {

    List<DrinkRecord> findByUserIdAndRecordDateOrderByCreatedAtDesc(Long userId, String recordDate);

    List<DrinkRecord> findByUserIdAndRecordDate(Long userId, String recordDate);
}
