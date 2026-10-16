package com.waterreminder.controller;

import com.waterreminder.dto.SettingsDTO;
import com.waterreminder.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<SettingsDTO> getSettings() {
        return ResponseEntity.ok(settingsService.getSettingsDTO());
    }

    @PutMapping
    public ResponseEntity<SettingsDTO> updateSettings(@RequestBody SettingsDTO dto) {
        return ResponseEntity.ok(settingsService.updateSettings(dto));
    }
}
