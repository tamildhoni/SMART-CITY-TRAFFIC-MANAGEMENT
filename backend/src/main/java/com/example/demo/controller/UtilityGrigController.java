package com.example.demo.controller;

import com.example.demo.entity.UtilityGrid;
import com.example.demo.service.UtilityGridService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilities")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UtilityGridController {

    private final UtilityGridService service;

    @GetMapping
    public ResponseEntity<List<UtilityGrid>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilityGrid> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<UtilityGrid> save(@RequestBody UtilityGrid grid) {
        return ResponseEntity.ok(service.save(grid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilityGrid> update(
            @PathVariable Long id,
            @RequestBody UtilityGrid grid) {
        return ResponseEntity.ok(service.update(id, grid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}