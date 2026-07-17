package com.example.demo.controller;

import com.example.demo.entity.FieldObservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FieldObservationController {
    
    
    public FieldObservationController(FieldObservationService observationService) {
        this.observationService = observationService;
    }
    
    @GetMapping
    public ResponseEntity<List<FieldObservation>> getAllObservations() {
        List<FieldObservation> observations = observationService.getAllObservations();
        return ResponseEntity.ok(observations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FieldObservation> getObservationById(@PathVariable Long id) {
        FieldObservation observation = observationService.getObservationById(id);
        return ResponseEntity.ok(observation);
    }
    
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<FieldObservation>> getObservationsByZone(@PathVariable Long zoneId) {
        List<FieldObservation> observations = observationService.getObservationsByZone(zoneId);
        return ResponseEntity.ok(observations);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('FIELD_TECHNICIAN', 'TRAFFIC_CONTROLLER', 'CITY_ADMINISTRATOR')")
    public ResponseEntity<FieldObservation> createObservation(@RequestBody FieldObservation observation, 
                                                              Authentication authentication) {
        String username = authentication.getName();
        FieldObservation created = observationService.createObservation(observation, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FIELD_TECHNICIAN', 'TRAFFIC_CONTROLLER', 'CITY_ADMINISTRATOR')")
    public ResponseEntity<FieldObservation> updateObservation(@PathVariable Long id, 
                                                              @RequestBody FieldObservation observation) {
        FieldObservation updated = observationService.updateObservation(id, observation);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'FIELD_TECHNICIAN')")
    public ResponseEntity<String> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        return ResponseEntity.ok("FieldObservation deleted successfully");
    }
}