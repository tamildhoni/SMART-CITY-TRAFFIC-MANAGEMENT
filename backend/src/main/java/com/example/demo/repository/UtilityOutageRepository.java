package com.example.demo.repository;

import com.example.demo.entity.UtilityOutage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilityOutageRepository extends JpaRepository<UtilityOutage, Long> {
    
    List<UtilityOutage> findByGrid_GridId(Long gridId);
    
    List<UtilityOutage> findByStatus(UtilityOutage.OutageStatus status);
}