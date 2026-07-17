package com.example.demo.controller;

import com.example.demo.entity.TrafficZone;
import com.example.demo.service.TrafficZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TrafficZoneController {
    
    private final TrafficZoneService zoneService;
    
    public TrafficZoneController(TrafficZoneService zoneService) {
        this.zoneService = zoneService;
    }
    
    @GetMapping
    public ResponseEntity<List<TrafficZone>> getAllZones() {
        List<TrafficZone> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrafficZone> getZoneById(@PathVariable Long id) {
        TrafficZone zone = zoneService.getZoneById(id);
        return ResponseEntity.ok(zone);
    }
    
    @GetMapping("/congested")
    public ResponseEntity<List<TrafficZone>> getHighlyCongestedZones() {
        List<TrafficZone> zones = zoneService.getHighlyCongestedZones();
        return ResponseEntity.ok(zones);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CITY_ADMINISTRATOR')")
    public ResponseEntity<String> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.ok("TrafficZone deleted successfully.");
    }
}