package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "traffic_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false, unique = true)
    private String zoneCode;

    @Column(nullable = false)
    private String district;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CongestionLevel currentCongestionLevel;

    @Column(nullable = false)
    private Integer signalCycleSeconds;
}