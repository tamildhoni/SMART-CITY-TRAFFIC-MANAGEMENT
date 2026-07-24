package com.example.demo.controller;

import com.example.demo.entity.AlertNotification;
import com.example.demo.entity.Role;
import com.example.demo.service.AlertNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AlertNotificationController {
    
    private final AlertNotificationService alertNotificationService;
    
    @GetMapping
    public ResponseEntity<List<AlertNotification>> getAlertsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        
        // Remove "ROLE_" prefix if present
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        
        Role userRole = Role.valueOf(role);
        List<AlertNotification> alerts = alertNotificationService.getAlertsByRole(userRole);
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<AlertNotification>> getAllAlerts() {
        return ResponseEntity.ok(alertNotificationService.getAllAlerts());
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAlertAsRead(@PathVariable Long id) {
        alertNotificationService.markAsRead(id);
        return ResponseEntity.ok("Alert marked as read.");
    }
    
    @PutMapping("/{id}/unread")
    public ResponseEntity<String> markAlertAsUnread(@PathVariable Long id) {
        alertNotificationService.markAsUnread(id);
        return ResponseEntity.ok("Alert marked as unread.");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Long id) {
        alertNotificationService.deleteAlert(id);
        return ResponseEntity.ok("Alert deleted successfully.");
    }
}