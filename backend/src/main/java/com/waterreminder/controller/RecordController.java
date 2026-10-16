package com.waterreminder.controller;

import com.waterreminder.dto.RecordDTO;
import com.waterreminder.service.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<List<RecordDTO>> getTodayRecords() {
        return ResponseEntity.ok(recordService.getTodayRecords());
    }

    @PostMapping
    public ResponseEntity<RecordDTO> addRecord(@RequestBody Map<String, Integer> body) {
        Integer amount = body.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recordService.addRecord(amount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
