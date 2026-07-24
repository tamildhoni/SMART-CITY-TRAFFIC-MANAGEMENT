package com.example.demo.service;

import com.example.demo.entity.AlertNotification;
import com.example.demo.entity.Role;
import com.example.demo.repository.AlertNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertNotificationService {
    
    private final AlertNotificationRepository alertNotificationRepository;
    
    public List<AlertNotification> getAlertsByRole(Role role) {
        return alertNotificationRepository.findByTargetRoleAndIsReadFalse(role);
    }
    
    public List<AlertNotification> getAllAlerts() {
        return alertNotificationRepository.findAll();
    }
    
    public AlertNotification markAsRead(Long alertId) {
        AlertNotification alert = alertNotificationRepository.findById(alertId)
            .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        alert.setRead(true);
        return alertNotificationRepository.save(alert);
    }
    
    public AlertNotification markAsUnread(Long alertId) {
        AlertNotification alert = alertNotificationRepository.findById(alertId)
            .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        alert.setRead(false);
        return alertNotificationRepository.save(alert);
    }
    
    public AlertNotification createAlert(AlertNotification alert) {
        return alertNotificationRepository.save(alert);
    }
    
    public void deleteAlert(Long alertId) {
        if (!alertNotificationRepository.existsById(alertId)) {
            throw new RuntimeException("Alert not found with id: " + alertId);
        }
        alertNotificationRepository.deleteById(alertId);
    }
}