package com.example.demo.controller;

import com.example.demo.entity.TrafficZone;
import com.example.demo.service.TrafficZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TrafficZoneController {
    
    private final TrafficZoneService trafficZoneService;
    
    @GetMapping
    public ResponseEntity<List<TrafficZone>> getAllZones() {
        return ResponseEntity.ok(trafficZoneService.getAllZones());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrafficZone> getZoneById(@PathVariable Long id) {
        return trafficZoneService.getZoneById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/congested")
    public ResponseEntity<List<TrafficZone>> getHighlyCongestedZones() {
        return ResponseEntity.ok(trafficZoneService.getHighlyCongestedZones());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CITY_ADMINISTRATOR')")
    public ResponseEntity<String> deleteZone(@PathVariable Long id) {
        trafficZoneService.deleteZone(id);
        return ResponseEntity.ok("TrafficZone deleted successfully.");
    }
}