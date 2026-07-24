package com.example.demo.service;

import com.example.demo.entity.TrafficZone;
import com.example.demo.repository.TrafficZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrafficZoneService {
    
    private final TrafficZoneRepository trafficZoneRepository;
    
    public List<TrafficZone> getAllZones() {
        return trafficZoneRepository.findAll();
    }
    
    public Optional<TrafficZone> getZoneById(Long id) {
        return trafficZoneRepository.findById(id);
    }
    
    public List<TrafficZone> getHighlyCongestedZones() {
        return trafficZoneRepository.findHighlyCongestedZones();
    }
    
    @Transactional
    public void deleteZone(Long id) {
        if (!trafficZoneRepository.existsById(id)) {
            throw new RuntimeException("Traffic zone not found with id: " + id);
        }
        trafficZoneRepository.deleteById(id);
    }
}