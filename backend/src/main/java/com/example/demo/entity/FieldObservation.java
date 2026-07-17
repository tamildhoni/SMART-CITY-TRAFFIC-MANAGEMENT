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
@Table(name = "field_observations")
public class FieldObservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long observationId;
    
    private String title;
    private String description;
    private String location;
    
    @ManyToOne
    @JoinColumn
    private TrafficZone zone;
    
    @ManyToOne
    @JoinColumn
    private CityUser reportedBy;
    
    private LocalDateTime observedAt = LocalDateTime.now();
    private String imageUrl;
}