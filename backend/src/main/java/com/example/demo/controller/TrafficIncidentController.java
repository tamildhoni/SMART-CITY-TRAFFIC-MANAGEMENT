package com.example.demo.controller;

import com.example.demo.dto.IncidentDto;
import com.example.demo.entity.TrafficIncident;
import com.example.demo.repository.TrafficIncidentRepository;
import com.example.demo.service.TrafficIncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TrafficIncidentController {
    
    private final TrafficIncidentService trafficIncidentService;
    private final TrafficIncidentRepository repository;  // Must be named "repository"
    
    @GetMapping
    public ResponseEntity<List<TrafficIncident>> getAllIncidents() {
        return ResponseEntity.ok(trafficIncidentService.getAllIncidents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrafficIncident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(trafficIncidentService.getIncidentById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'TRAFFIC_CONTROLLER')")
    public ResponseEntity<String> createIncident(@Valid @RequestBody IncidentDto incidentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        trafficIncidentService.reportIncident(incidentDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body("TrafficIncident created successfully.");
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'TRAFFIC_CONTROLLER')")
    public ResponseEntity<String> updateIncident(@PathVariable Long id, @Valid @RequestBody IncidentDto incidentDto) {
        trafficIncidentService.updateIncident(id, incidentDto);
        return ResponseEntity.ok("TrafficIncident updated successfully.");
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CITY_ADMINISTRATOR')")
    public ResponseEntity<String> deleteIncident(@PathVariable Long id) {
        trafficIncidentService.deleteIncident(id);
        return ResponseEntity.ok("TrafficIncident deleted successfully.");
    }
    
    @PutMapping("/{id}/dispatch")
    @PreAuthorize("hasRole('TRAFFIC_CONTROLLER')")
    public ResponseEntity<TrafficIncident> dispatchIncident(@PathVariable Long id) {
        TrafficIncident updatedIncident = trafficIncidentService.dispatchResponse(id);
        return ResponseEntity.ok(updatedIncident);
    }
}