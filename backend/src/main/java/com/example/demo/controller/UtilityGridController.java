package com.example.demo.controller;

import com.example.demo.entity.UtilityGrid;
import com.example.demo.entity.UtilityOutage;
import com.example.demo.service.UtilityGridService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grids")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UtilityGridController {
    
    private final UtilityGridService gridService;
    
    public UtilityGridController(UtilityGridService gridService) {
        this.gridService = gridService;
    }
    
    @GetMapping
    public ResponseEntity<List<UtilityGrid>> getAllGrids() {
        return ResponseEntity.ok(gridService.getAllGrids());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UtilityGrid> getGridById(@PathVariable Long id) {
        return ResponseEntity.ok(gridService.getGridById(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('UTILITY_SUPERVISOR')")
    public ResponseEntity<UtilityGrid> updateGridLoad(@PathVariable Long id, @RequestBody Double currentLoad) {
        gridService.updateLoad(id, currentLoad);
        return ResponseEntity.ok(gridService.getGridById(id));
    }
    
    @PostMapping("/{id}/outages")
    @PreAuthorize("hasRole('UTILITY_SUPERVISOR')")
    public ResponseEntity<UtilityOutage> registerOutage(@PathVariable Long id, @RequestBody UtilityOutage outage) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gridService.registerOutage(id, outage));
    }
}