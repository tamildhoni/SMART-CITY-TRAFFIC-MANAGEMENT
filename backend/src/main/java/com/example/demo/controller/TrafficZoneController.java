package com.example.demo.service;

import com.example.demo.entity.TrafficZone;
import com.example.demo.repository.TrafficZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrafficZoneService {
    private final TrafficZoneRepository zoneRepository;
    
    public TrafficZoneService(TrafficZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }
    
    public List<TrafficZone> getAllZones() {
        return zoneRepository.findAll();
    }
    
    public TrafficZone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrafficZone not found"));
    }
    
    public List<TrafficZone> getHighlyCongestedZones() {
        return zoneRepository.findHighlyCongestedZones();
    }
    
    @Transactional
    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }
}