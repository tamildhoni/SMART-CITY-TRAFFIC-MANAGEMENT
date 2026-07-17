package com.example.demo.controller;

import com.example.demo.entity.AlertNotification;
import com.example.demo.service.AlertNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AlertNotificationController {
    
    private final AlertNotificationService alertService;
    
    public AlertNotificationController(AlertNotificationService alertService) {
        this.alertService = alertService;
    }
    
    @GetMapping
    public ResponseEntity<List<AlertNotification>> getAlertsForUser(Authentication authentication) {
        String username = authentication.getName();
        List<AlertNotification> alerts = alertService.getAlertsForUser(username);
        return ResponseEntity.ok(alerts);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAlertAsRead(@PathVariable Long id) {
        alertService.markAlertAsRead(id);
        return ResponseEntity.ok("Alert marked as read.");
    }
}