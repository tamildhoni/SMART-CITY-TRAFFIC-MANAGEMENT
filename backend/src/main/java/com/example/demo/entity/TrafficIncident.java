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
@Table(name = "traffic_incidents")
public class TrafficIncident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incidentId;
    
    @Column(nullable = false)
    private String title;
    
    @Enumerated(EnumType.STRING)
    private IncidentType incidentType;
    
    @Enumerated(EnumType.STRING)
    private Severity severity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status = IncidentStatus.REPORTED;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private TrafficZone zone;
    
    private LocalDateTime reportedAt = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn
    private CityUser reportedBy;
    
    private String description;
    
    // Inner enum for incident types
    public enum IncidentType {
        ACCIDENT,
        ROAD_CLOSURE,
        TRAFFIC_JAM,
        CONSTRUCTION,
        WEATHER,
        OTHER
    }
    
    // Inner enum for severity levels
    public enum Severity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
    
    // Inner enum for incident status
    public enum IncidentStatus {
        REPORTED,
        DISPATCHED,
        RESOLVED,
        CLOSED
    }
}