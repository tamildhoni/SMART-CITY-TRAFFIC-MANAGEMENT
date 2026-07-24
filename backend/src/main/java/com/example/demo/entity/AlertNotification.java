package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alert_notifications")
public class AlertNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;
    @Enumerated(EnumType.STRING)
    private Role targetRole;
    @Column(nullable = false, length = 500)
    private String message;
    private String relatedEntityType;
    private Long relatedEntityId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Severity severity;
    private boolean isRead = false;
    
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
}