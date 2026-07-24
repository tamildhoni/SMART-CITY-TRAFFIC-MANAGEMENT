package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private GridType gridType;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OutageType outageType;
    
    private String affectedArea;
    private LocalDateTime startTime;
    
    @Enumerated(EnumType.STRING)
    private OutageStatus status;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    public enum GridType {
        ELECTRICITY, WATER, GAS, SEWAGE
    }
    
    public enum OutageType {
        PLANNED, UNPLANNED, EMERGENCY
    }
    
    public enum OutageStatus {
        ACTIVE, RESOLVED, RESTORED
    }
    
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}