package com.example.demo.service;

import com.example.demo.dto.OutageDto;
import com.example.demo.entity.*;
import com.example.demo.repository.UtilityGridRepository;
import com.example.demo.repository.UtilityOutageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UtilityGridService {
    private final UtilityGridRepository gridRepository;
    private final UtilityOutageRepository outageRepository;
    
    public UtilityGridService(UtilityGridRepository gridRepository, UtilityOutageRepository outageRepository) {
        this.gridRepository = gridRepository;
        this.outageRepository = outageRepository;
    }
    
    public void updateLoad(Long id, Double newLoad) {
        UtilityGrid grid = gridRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grid not found"));
        
        grid.setCurrentLoad(newLoad);
        
        // Auto-transition to DEGRADED if load exceeds 90% capacity
        if (newLoad > grid.getCapacityUnits() * 0.9) {
            grid.setStatus(GridStatus.DEGRADED);
        } else if (grid.getStatus() == GridStatus.DEGRADED && newLoad <= grid.getCapacityUnits() * 0.9) {
            grid.setStatus(GridStatus.OPERATIONAL);
        }
        
        gridRepository.save(grid);
    }
    
    public UtilityOutage registerOutage(Long gridId, OutageDto dto) {
        UtilityGrid grid = gridRepository.findById(gridId)
                .orElseThrow(() -> new RuntimeException("Grid not found"));
        
        // Update grid status to DEGRADED
        grid.setStatus(GridStatus.DEGRADED);
        gridRepository.save(grid);
        
        UtilityOutage outage = new UtilityOutage();
        outage.setGrid(grid);
        outage.setOutageType(OutageType.valueOf(dto.getOutageType()));
        outage.setAffectedArea(dto.getPerformanceType());
        outage.setSeverity(Severity.valueOf(dto.getSeverity()));
        outage.setDescription(dto.getDescription());
        
        return outageRepository.save(outage);
    }
    
    public List<UtilityGrid> getAllGrids() {
        return gridRepository.findAll();
    }
    
    public UtilityGrid getGridById(Long id) {
        return gridRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UtilityGrid not found"));
    }
}