package com.example.demo.controller;

import com.example.demo.dto.IncidentDto;
import com.example.demo.entity.TrafficIncident;
import com.example.demo.service.TrafficIncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TrafficIncidentController {
    
    private final TrafficIncidentService incidentService;
    
    public TrafficIncidentController(TrafficIncidentService incidentService) {
        this.incidentService = incidentService;
    }
    
    @GetMapping
    public ResponseEntity<List<TrafficIncident>> getAllIncidents() {
        List<TrafficIncident> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrafficIncident> getIncidentById(@PathVariable Long id) {
        TrafficIncident incident = incidentService.getIncidentById(id);
        return ResponseEntity.ok(incident);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'TRAFFIC_CONTROLLER')")
    public ResponseEntity<String> createIncident(@Valid @RequestBody IncidentDto dto, Authentication authentication) {
        String username = authentication.getName();
        TrafficIncident incident = incidentService.reportIncident(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("TrafficIncident created successfully");
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'TRAFFIC_CONTROLLER')")
    public ResponseEntity<String> updateIncident(@PathVariable Long id, @Valid @RequestBody IncidentDto dto) {
        incidentService.updateIncident(id, dto);
        return ResponseEntity.ok("TrafficIncident updated successfully");
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CITY_ADMINISTRATOR')")
    public ResponseEntity<String> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.ok("TrafficIncident deleted successfully");
    }
    
    @PutMapping("/{id}/dispatch")
    @PreAuthorize("hasRole('TRAFFIC_CONTROLLER')")
    public ResponseEntity<TrafficIncident> dispatchIncident(@PathVariable Long id) {
        TrafficIncident incident = incidentService.dispatchResponse(id);
        return ResponseEntity.ok(incident);
    }
}