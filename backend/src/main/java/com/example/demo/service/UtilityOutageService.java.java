package com.example.demo.service;

import com.example.demo.entity.UtilityOutage;
import com.example.demo.repository.UtilityOutageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilityOutageService {
    
    private final UtilityOutageRepository outageRepository;
    
    public List<UtilityOutage> getAllOutages() {
        return outageRepository.findAll();
    }
    
    public UtilityOutage getOutageById(Long id) {
        return outageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utility outage not found with id: " + id));
    }
    
    public List<UtilityOutage> getOutagesByGrid(Long gridId) {
        return outageRepository.findByGrid_GridId(gridId);
    }
    
    public List<UtilityOutage> getActiveOutages() {
        return outageRepository.findByStatus(UtilityOutage.OutageStatus.ACTIVE);
    }
    
    public UtilityOutage resolveOutage(Long id) {
        UtilityOutage outage = outageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utility outage not found with id: " + id));
        outage.setStatus(UtilityOutage.OutageStatus.RESOLVED);
        return outageRepository.save(outage);
    }
}