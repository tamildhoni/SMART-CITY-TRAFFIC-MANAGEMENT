package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "traffic_zones")
public class TrafficZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;
    @Column(nullable = false)
    private String zoneName;
    @Column(unique = true)
    private String zoneCode;
    private String district;
    @Enumerated(EnumType.STRING)
    private CongestionLevel currentCongestionLevel;
    private Integer signalCycleSeconds;
    
    public enum CongestionLevel { LOW, MODERATE, HIGH, CRITICAL }
}