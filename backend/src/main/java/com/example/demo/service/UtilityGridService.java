package com.example.demo.service;

import com.example.demo.entity.UtilityGrid;
import com.example.demo.entity.UtilityOutage;
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
        
        if (newLoad > grid.getCapacityUnits() * 0.9) {
            grid.setStatus(UtilityGrid.GridStatus.DEGRADED);
        } else if (grid.getStatus() == UtilityGrid.GridStatus.DEGRADED && newLoad <= grid.getCapacityUnits() * 0.9) {
            grid.setStatus(UtilityGrid.GridStatus.OPERATIONAL);
        }
        
        gridRepository.save(grid);
    }
    
    public UtilityOutage registerOutage(Long gridId, UtilityOutage outage) {
        UtilityGrid grid = gridRepository.findById(gridId)
                .orElseThrow(() -> new RuntimeException("Grid not found"));
        
        grid.setStatus(UtilityGrid.GridStatus.DEGRADED);
        gridRepository.save(grid);
        
        outage.setGrid(grid);
        outage.setStatus(UtilityOutage.OutageStatus.ACTIVE);
        
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