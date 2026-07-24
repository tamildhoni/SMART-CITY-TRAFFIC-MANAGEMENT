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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;
    @ManyToOne
    @JoinColumn(nullable = false)
    private TrafficZone zone;
    private LocalDateTime reportedAt;
    @ManyToOne
    private CityUser reportedBy;
    private String description;
    
    public enum IncidentType { ACCIDENT, TRAFFIC_JAM, ROAD_CLOSURE, CONSTRUCTION, OTHER }
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
    public enum IncidentStatus { REPORTED, DISPATCHED, RESOLVED, CLOSED }
}