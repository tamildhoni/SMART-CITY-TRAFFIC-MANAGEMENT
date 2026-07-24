package com.example.demo.controller;

import com.example.demo.entity.UtilityOutage;
import com.example.demo.service.UtilityOutageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outages")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UtilityOutageController {
    
    private final UtilityOutageService utilityOutageService;
    
    @GetMapping
    public ResponseEntity<List<UtilityOutage>> getAllOutages() {
        return ResponseEntity.ok(utilityOutageService.getAllOutages());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UtilityOutage> getOutageById(@PathVariable Long id) {
        return ResponseEntity.ok(utilityOutageService.getOutageById(id));
    }
    
    @GetMapping("/grid/{gridId}")
    public ResponseEntity<List<UtilityOutage>> getOutagesByGrid(@PathVariable Long gridId) {
        return ResponseEntity.ok(utilityOutageService.getOutagesByGrid(gridId));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<UtilityOutage>> getActiveOutages() {
        return ResponseEntity.ok(utilityOutageService.getActiveOutages());
    }
    
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('UTILITY_SUPERVISOR')")
    public ResponseEntity<UtilityOutage> resolveOutage(@PathVariable Long id) {
        return ResponseEntity.ok(utilityOutageService.resolveOutage(id));
    }
}