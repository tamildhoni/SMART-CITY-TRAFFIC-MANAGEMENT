package com.example.demo.repository;

import com.example.demo.entity.FieldObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldObservationRepository extends JpaRepository<FieldObservation, Long> {
    
    List<FieldObservation> findByZone_ZoneId(Long zoneId);
    
    List<FieldObservation> findByReportedBy_UserId(Long userId);
}