package com.example.demo.repository;

import com.example.demo.entity.FieldObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldObservationRepository extends JpaRepository<FieldObservation, Long> {
    
    // Since zone is a String in FieldObservation entity
    List<FieldObservation> findByZone(String zone);
    
    // Optional: search by zone containing text (partial match)
    List<FieldObservation> findByZoneContaining(String zone);
    
    // reportedBy is a CityUser entity with userId
    List<FieldObservation> findByReportedBy_UserId(Long userId);
}