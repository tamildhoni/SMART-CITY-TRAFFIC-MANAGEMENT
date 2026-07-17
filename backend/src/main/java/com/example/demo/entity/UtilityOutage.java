package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "utility_outages")
public class UtilityOutage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outageId;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private UtilityGrid grid;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutageType outageType;
    
    private String affectedArea;
    private LocalDateTime startTime = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private OutageStatus status = OutageStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    private String description;
    
    // Inner enum for outage types
    public enum OutageType {
        PLANNED,
        UNPLANNED,
        EMERGENCY,
        MAINTENANCE
    }
    
    // Inner enum for outage status
    public enum OutageStatus {
        ACTIVE,
        RESOLVED,
        RESTORED
    }
}