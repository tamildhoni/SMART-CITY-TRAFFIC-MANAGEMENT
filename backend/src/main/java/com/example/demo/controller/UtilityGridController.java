package com.example.demo.controller;

import com.example.demo.dto.OutageDto;
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
        List<UtilityGrid> grids = gridService.getAllGrids();
        return ResponseEntity.ok(grids);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UtilityGrid> getGridById(@PathVariable Long id) {
        UtilityGrid grid = gridService.getGridById(id);
        return ResponseEntity.ok(grid);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('UTILITY_SUPERVISOR')")
    public ResponseEntity<UtilityGrid> updateGridLoad(@PathVariable Long id, @RequestBody Double currentLoad) {
        gridService.updateLoad(id, currentLoad);
        UtilityGrid grid = gridService.getGridById(id);
        return ResponseEntity.ok(grid);
    }
    
    @PostMapping("/{id}/outages")
    @PreAuthorize("hasRole('UTILITY_SUPERVISOR')")
    public ResponseEntity<UtilityOutage> registerOutage(@PathVariable Long id, @RequestBody OutageDto dto) {
        UtilityOutage outage = gridService.registerOutage(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(outage);
    }
}