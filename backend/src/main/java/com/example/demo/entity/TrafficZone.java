package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    
    private String zoneCode;
    private String district;
    
    @Enumerated(EnumType.STRING)
    private CongestionLevel currentCongestionLevel = CongestionLevel.LOW;
    
    private Integer signalCycleSeconds = 60;
}