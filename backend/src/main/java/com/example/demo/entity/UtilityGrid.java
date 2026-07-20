package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "utility_grids")
public class UtilityGrid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gridId;
    
    @Column(nullable = false)
    private String gridName;
    
    @Enumerated(EnumType.STRING)
    private GridType gridType;
    
    private String district;
    
    @Column(nullable = false)
    private Double capacityUnits;
    
    private Double currentLoad = 0.0;
    
    @Enumerated(EnumType.STRING)
    private GridStatus status = GridStatus.OPERATIONAL;
    
    public enum GridType {
        ELECTRICITY, WATER, GAS, SEWAGE, TELECOM
    }
    
    public enum GridStatus {
        OPERATIONAL, DEGRADED, OFFLINE
    }
}