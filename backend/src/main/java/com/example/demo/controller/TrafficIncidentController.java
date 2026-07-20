package com.example.demo.controller;

import com.example.demo.entity.TrafficIncident;
import com.example.demo.service.TrafficIncidentService;
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
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrafficIncident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'TRAFFIC_CONTROLLER')")
    public ResponseEntity<String> createIncident(@RequestBody TrafficIncident incident, Authentication authentication) {
        incidentService.reportIncident(incident, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("TrafficIncident created successfully");
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CITY_ADMINISTRATOR', 'TRAFFIC_CONTROLLER')")
    public ResponseEntity<String> updateIncident(@PathVariable Long id, @RequestBody TrafficIncident incident) {
        incidentService.updateIncident(id, incident);
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
        return ResponseEntity.ok(incidentService.dispatchResponse(id));
    }
}