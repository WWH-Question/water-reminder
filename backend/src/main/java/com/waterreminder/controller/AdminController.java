package com.waterreminder.controller;

import com.waterreminder.dto.AdminUserDTO;
import com.waterreminder.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminUserDTO> updateUser(@PathVariable Long id, @RequestBody AdminUserDTO dto) {
        return ResponseEntity.ok(adminService.updateUserSettings(id, dto));
    }
}
