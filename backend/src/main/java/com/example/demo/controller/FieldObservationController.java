package com.example.demo.controller;

import com.example.demo.entity.CityUser;
import com.example.demo.entity.FieldObservation;
import com.example.demo.repository.FieldObservationRepository;
import com.example.demo.repository.CityUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/observations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FieldObservationController {
    
    private final FieldObservationRepository observationRepository;
    private final CityUserRepository userRepository;
    
    public FieldObservationController(FieldObservationRepository observationRepository,
                                      CityUserRepository userRepository) {
        this.observationRepository = observationRepository;
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<FieldObservation>> getAllObservations() {
        List<FieldObservation> observations = observationRepository.findAll();
        return ResponseEntity.ok(observations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FieldObservation> getObservationById(@PathVariable Long id) {
        FieldObservation observation = observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldObservation not found with id: " + id));
        return ResponseEntity.ok(observation);
    }
    
    // FIXED: Changed from zoneId (Long) to zone (String)
    // Now uses findByZone(String zone) from repository
    @GetMapping("/zone/{zone}")
    public ResponseEntity<List<FieldObservation>> getObservationsByZone(@PathVariable String zone) {
        List<FieldObservation> observations = observationRepository.findByZone(zone);
        return ResponseEntity.ok(observations);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('FIELD_TECHNICIAN', 'TRAFFIC_CONTROLLER', 'CITY_ADMINISTRATOR')")
    public ResponseEntity<FieldObservation> createObservation(@RequestBody FieldObservation observation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        CityUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        observation.setReportedBy(user);
        observation.setReportedAt(LocalDateTime.now());
        
        FieldObservation created = observationRepository.save(observation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FIELD_TECHNICIAN', 'TRAFFIC_CONTROLLER', 'CITY_ADMINISTRATOR')")
    public ResponseEntity<FieldObservation> updateObservation(@PathVariable Long id, 
                                                              @RequestBody FieldObservation observation) {
        FieldObservation existing = observationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FieldObservation not found with id: " + id));
        
        existing.setTitle(observation.getTitle());
        existing.setDescription(observation.getDescription());
        existing.setLocation(observation.getLocation());
        existing.setZone(observation.getZone());
        existing.setImageUrl(observation.getImageUrl());
        
        FieldObservation updated = observationRepository.save(existing);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'FIELD_TECHNICIAN')")
    public ResponseEntity<String> deleteObservation(@PathVariable Long id) {
        if (!observationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        observationRepository.deleteById(id);
        return ResponseEntity.ok("FieldObservation deleted successfully");
    }
}