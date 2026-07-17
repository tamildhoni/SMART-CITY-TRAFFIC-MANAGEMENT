package com.example.demo.service;

import com.example.demo.dto.DashboardStatsDto;
import com.example.demo.entity.Role;
import com.example.demo.repository.AlertNotificationRepository;
import com.example.demo.repository.TrafficIncidentRepository;
import com.example.demo.repository.TrafficZoneRepository;
import com.example.demo.repository.UtilityOutageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {
    private final TrafficIncidentRepository incidentRepository;
    private final UtilityOutageRepository outageRepository;
    private final TrafficZoneRepository zoneRepository;
    private final AlertNotificationRepository alertRepository;
    
    public AnalyticsService(TrafficIncidentRepository incidentRepository,
                            UtilityOutageRepository outageRepository,
                            TrafficZoneRepository zoneRepository,
                            AlertNotificationRepository alertRepository) {
        this.incidentRepository = incidentRepository;
        this.outageRepository = outageRepository;
        this.zoneRepository = zoneRepository;
        this.alertRepository = alertRepository;
    }
    
    public DashboardStatsDto getCityStats() {
        long totalIncidents = incidentRepository.count();
        long activeOutages = outageRepository.count();
        long congestedZones = zoneRepository.findHighlyCongestedZones().size();
        long criticalAlerts = alertRepository.findByTargetRoleAndIsReadFalse(Role.CITY_ADMINISTRATOR).size();
        
        return new DashboardStatsDto(totalIncidents, activeOutages, congestedZones, criticalAlerts);
    }
}