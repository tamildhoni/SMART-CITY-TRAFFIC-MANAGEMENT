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
@Table(name = "field_observations")
public class FieldObservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long observationId;
    
    private String title;
    private String description;
    private String location;
    private String zone;
    private String imageUrl;
    
    @ManyToOne
    private CityUser reportedBy;
    
    private LocalDateTime reportedAt;
}