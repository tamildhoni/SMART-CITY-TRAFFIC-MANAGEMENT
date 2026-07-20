package com.example.demo.controller;

import com.example.demo.entity.UtilityOutage;
import com.example.demo.service.UtilityOutageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outages")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UtilityOutageController {
    
    private final UtilityOutageService outageService;
    
    public UtilityOutageController(UtilityOutageService outageService) {
        this.outageService = outageService;
    }
    
    @GetMapping
    public ResponseEntity<List<UtilityOutage>> getAllOutages() {
        List<UtilityOutage> outages = outageService.getAllOutages();
        return ResponseEntity.ok(outages);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UtilityOutage> getOutageById(@PathVariable Long id) {
        UtilityOutage outage = outageService.getOutageById(id);
        return ResponseEntity.ok(outage);
    }
    
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('UTILITY_SUPERVISOR')")
    public ResponseEntity<UtilityOutage> resolveOutage(@PathVariable Long id) {
        UtilityOutage outage = outageService.resolveOutage(id);
        return ResponseEntity.ok(outage);
    }
}