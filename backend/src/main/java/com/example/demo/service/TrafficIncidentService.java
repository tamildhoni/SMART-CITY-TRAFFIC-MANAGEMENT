package com.example.demo.service;

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
    
    public TrafficIncident reportIncident(TrafficIncident incident, String username) {
        TrafficZone zone = zoneRepository.findById(incident.getZone().getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        CityUser reporter = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        incident.setZone(zone);
        incident.setReportedBy(reporter);
        incident.setStatus(TrafficIncident.IncidentStatus.REPORTED);
        
        TrafficIncident saved = incidentRepository.save(incident);
        
        // Create alert for HIGH or CRITICAL severity
        if (incident.getSeverity() == TrafficIncident.Severity.HIGH || 
            incident.getSeverity() == TrafficIncident.Severity.CRITICAL) {
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
        
        if (incident.getStatus() != TrafficIncident.IncidentStatus.REPORTED) {
            throw new IllegalStateException("Incident must be in REPORTED state to dispatch");
        }
        
        incident.setStatus(TrafficIncident.IncidentStatus.DISPATCHED);
        return incidentRepository.save(incident);
    }
    
    public List<TrafficIncident> getAllIncidents() {
        return incidentRepository.findAll();
    }
    
    public TrafficIncident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrafficIncident not found"));
    }
    
    public TrafficIncident updateIncident(Long id, TrafficIncident incidentDetails) {
        TrafficIncident incident = getIncidentById(id);
        incident.setTitle(incidentDetails.getTitle());
        incident.setIncidentType(incidentDetails.getIncidentType());
        incident.setSeverity(incidentDetails.getSeverity());
        incident.setDescription(incidentDetails.getDescription());
        
        if (incidentDetails.getZone() != null && incidentDetails.getZone().getZoneId() != null) {
            TrafficZone zone = zoneRepository.findById(incidentDetails.getZone().getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone not found"));
            incident.setZone(zone);
        }
        
        return incidentRepository.save(incident);
    }
    
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}