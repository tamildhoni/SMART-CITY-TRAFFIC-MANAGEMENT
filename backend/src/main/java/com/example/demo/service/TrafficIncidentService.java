package com.example.demo.service;

import com.example.demo.dto.IncidentDto;
import com.example.demo.entity.*;
import com.example.demo.repository.TrafficIncidentRepository;
import com.example.demo.repository.TrafficZoneRepository;
import com.example.demo.repository.CityUserRepository;
import com.example.demo.repository.AlertNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrafficIncidentService {
    private final TrafficIncidentRepository incidentRepository;
    private final TrafficZoneRepository zoneRepository;
    private final CityUserRepository userRepository;
    private final AlertNotificationRepository alertRepository;
    
    public TrafficIncidentService(TrafficIncidentRepository incidentRepository,
                                  TrafficZoneRepository zoneRepository,
                                  CityUserRepository userRepository,
                                  AlertNotificationRepository alertRepository) {
        this.incidentRepository = incidentRepository;
        this.zoneRepository = zoneRepository;
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
    }
    
    public TrafficIncident reportIncident(IncidentDto dto, String username) {
        TrafficZone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        CityUser reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        TrafficIncident incident = new TrafficIncident();
        incident.setTitle(dto.getTitle());
        incident.setIncidentType(IncidentType.valueOf(dto.getIncidentType()));
        incident.setSeverity(Severity.valueOf(dto.getSeverity()));
        incident.setZone(zone);
        incident.setReportedBy(reporter);
        incident.setDescription(dto.getDescription());
        incident.setStatus(IncidentStatus.REPORTED);
        
        TrafficIncident saved = incidentRepository.save(incident);
        
        // Create alert for HIGH or CRITICAL severity
        if (incident.getSeverity() == Severity.HIGH || incident.getSeverity() == Severity.CRITICAL) {
            AlertNotification alert = new AlertNotification();
            alert.setTargetRole(Role.TRAFFIC_CONTROLLER);
            alert.setMessage("High severity incident reported: " + incident.getTitle());
            alert.setRelatedEntityType("TrafficIncident");
            alert.setRelatedEntityId(incident.getIncidentId());
            alert.setSeverity(incident.getSeverity());
            alertRepository.save(alert);
        }
        
        return saved;
    }
    
    public TrafficIncident dispatchResponse(Long id) {
        TrafficIncident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        
        if (incident.getStatus() != IncidentStatus.REPORTED) {
            throw new IllegalStateException("Incident must be in REPORTED state to dispatch");
        }
        
        incident.setStatus(IncidentStatus.DISPATCHED);
        return incidentRepository.save(incident);
    }
    
    public List<TrafficIncident> getAllIncidents() {
        return incidentRepository.findAll();
    }
    
    public TrafficIncident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrafficIncident not found"));
    }
    
    public TrafficIncident updateIncident(Long id, IncidentDto dto) {
        TrafficIncident incident = getIncidentById(id);
        incident.setTitle(dto.getTitle());
        incident.setIncidentType(IncidentType.valueOf(dto.getIncidentType()));
        incident.setSeverity(Severity.valueOf(dto.getSeverity()));
        incident.setDescription(dto.getDescription());
        
        if (dto.getZoneId() != null) {
            TrafficZone zone = zoneRepository.findById(dto.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone not found"));
            incident.setZone(zone);
        }
        
        return incidentRepository.save(incident);
    }
    
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}