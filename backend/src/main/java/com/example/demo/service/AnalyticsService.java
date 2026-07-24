package com.example.demo.service;

import com.example.demo.dto.DashboardStatsDto;
import com.example.demo.entity.TrafficIncident;
import com.example.demo.entity.UtilityOutage;
import com.example.demo.repository.TrafficIncidentRepository;
import com.example.demo.repository.TrafficZoneRepository;
import com.example.demo.repository.UtilityOutageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {
    
    private final TrafficIncidentRepository incidentRepository;
    private final TrafficZoneRepository zoneRepository;
    private final UtilityOutageRepository outageRepository;
    
    public DashboardStatsDto getCityStats() {
        long totalIncidents = incidentRepository.count();
        long activeOutages = outageRepository.findByStatus(UtilityOutage.OutageStatus.ACTIVE).size();
        long congestedZones = zoneRepository.findHighlyCongestedZones().size();
        long criticalAlerts = 0;
        
        return new DashboardStatsDto(totalIncidents, activeOutages, congestedZones, criticalAlerts);
    }
}