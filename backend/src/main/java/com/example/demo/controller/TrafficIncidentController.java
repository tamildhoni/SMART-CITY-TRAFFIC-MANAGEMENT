package com.example.demo.controller;

import com.example.demo.entity.TrafficIncident;
import com.example.demo.service.TrafficIncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TrafficIncidentController {

    private final TrafficIncidentService trafficIncidentService;

    @GetMapping
    public ResponseEntity<List<TrafficIncident>> getAllIncidents() {
        return ResponseEntity.ok(trafficIncidentService.getAllIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrafficIncident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(trafficIncidentService.getIncidentById(id));
    }

    @PostMapping
    public ResponseEntity<TrafficIncident> createIncident(
            @RequestBody TrafficIncident incident) {

        return ResponseEntity.ok(
                trafficIncidentService.createIncident(incident)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrafficIncident> updateIncident(
            @PathVariable Long id,
            @RequestBody TrafficIncident incident) {

        return ResponseEntity.ok(
                trafficIncidentService.updateIncident(id, incident)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncident(@PathVariable Long id) {

        trafficIncidentService.deleteIncident(id);

        return ResponseEntity.ok("Traffic Incident deleted successfully");
    }
}