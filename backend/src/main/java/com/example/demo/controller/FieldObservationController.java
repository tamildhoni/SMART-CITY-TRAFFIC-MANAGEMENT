package com.example.demo.service;

import com.example.demo.entity.CityUser;
import com.example.demo.entity.FieldObservation;
import com.example.demo.repository.FieldObservationRepository;
import com.example.demo.repository.CityUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FieldObservationService {
    
    private final FieldObservationRepository observationRepository;
    private final CityUserRepository userRepository;
    
    public FieldObservationService(FieldObservationRepository observationRepository,
                                   CityUserRepository userRepository) {
        this.observationRepository = observationRepository;
        this.userRepository = userRepository;
    }
    
    public List<FieldObservation> getAllObservations() {
        return observationRepository.findAll();
    }
    
    public FieldObservation getObservationById(Long id) {
        return observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldObservation not found"));
    }
    
    public List<FieldObservation> getObservationsByZone(Long zoneId) {
        return observationRepository.findByZone_ZoneId(zoneId);
    }
    
    public FieldObservation createObservation(FieldObservation observation, String username) {
        CityUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        observation.setReportedBy(user);
        return observationRepository.save(observation);
    }
    
    public FieldObservation updateObservation(Long id, FieldObservation observation) {
        FieldObservation existing = getObservationById(id);
        existing.setTitle(observation.getTitle());
        existing.setDescription(observation.getDescription());
        existing.setLocation(observation.getLocation());
        existing.setZone(observation.getZone());
        existing.setImageUrl(observation.getImageUrl());
        return observationRepository.save(existing);
    }
    
    public void deleteObservation(Long id) {
        observationRepository.deleteById(id);
    }
}