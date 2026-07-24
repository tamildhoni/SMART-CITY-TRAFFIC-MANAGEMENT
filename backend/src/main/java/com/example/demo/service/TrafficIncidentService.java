package com.example.demo.service;

import com.example.demo.dto.IncidentDto;
import com.example.demo.entity.*;
import com.example.demo.repository.AlertNotificationRepository;
import com.example.demo.repository.TrafficIncidentRepository;
import com.example.demo.repository.TrafficZoneRepository;
import com.example.demo.repository.CityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TrafficIncidentService {
    
    private final TrafficIncidentRepository incidentRepository;
    private final TrafficZoneRepository zoneRepository;
    private final CityUserRepository userRepository;
    private final AlertNotificationRepository alertRepository;
    
    public TrafficIncident reportIncident(IncidentDto dto, String username) {
        TrafficZone zone = zoneRepository.findById(dto.getZoneId())
            .orElseThrow(() -> new RuntimeException("Traffic zone not found with id: " + dto.getZoneId()));
        
        CityUser reporter = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        TrafficIncident incident = new TrafficIncident();
        incident.setTitle(dto.getTitle());
        incident.setIncidentType(TrafficIncident.IncidentType.valueOf(dto.getIncidentType()));
        incident.setSeverity(TrafficIncident.Severity.valueOf(dto.getSeverity()));
        incident.setZone(zone);
        incident.setReportedBy(reporter);
        incident.setStatus(TrafficIncident.IncidentStatus.REPORTED);
        incident.setReportedAt(LocalDateTime.now());
        incident.setDescription(dto.getDescription());
        
        TrafficIncident savedIncident = incidentRepository.save(incident);
        
        if (savedIncident.getSeverity() == TrafficIncident.Severity.HIGH || 
            savedIncident.getSeverity() == TrafficIncident.Severity.CRITICAL) {
            
            AlertNotification alert = new AlertNotification();
            alert.setTargetRole(Role.TRAFFIC_CONTROLLER);
            alert.setMessage("High severity incident reported: " + savedIncident.getTitle());
            alert.setRelatedEntityType("TrafficIncident");
            alert.setRelatedEntityId(savedIncident.getIncidentId());
            alert.setSeverity(AlertNotification.Severity.valueOf(savedIncident.getSeverity().name()));
            
            alertRepository.save(alert);
        }
        
        return savedIncident;
    }
    
    public TrafficIncident dispatchResponse(Long id) {
        TrafficIncident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Traffic incident not found with id: " + id));
        
        if (incident.getStatus() != TrafficIncident.IncidentStatus.REPORTED) {
            throw new IllegalStateException("Incident must be in REPORTED state to dispatch. Current status: " + incident.getStatus());
        }
        
        incident.setStatus(TrafficIncident.IncidentStatus.DISPATCHED);
        return incidentRepository.save(incident);
    }
    
    public List<TrafficIncident> getAllEndpoint() {
        return incidentRepository.findAll();
    }
    
    public TrafficIncident getIncidentById(Long id) {
        return incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Traffic incident not found with id: " + id));
    }
    
    public TrafficIncident updateIncident(Long id, IncidentDto dto) {
        TrafficIncident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Traffic incident not found with id: " + id));
        
        TrafficZone zone = zoneRepository.findById(dto.getZoneId())
            .orElseThrow(() -> new RuntimeException("Traffic zone not found with id: " + dto.getZoneId()));
        
        incident.setTitle(dto.getTitle());
        incident.setIncidentType(TrafficIncident.IncidentType.valueOf(dto.getIncidentType()));
        incident.setSeverity(TrafficIncident.Severity.valueOf(dto.getSeverity()));
        incident.setZone(zone);
        incident.setDescription(dto.getDescription());
        
        return incidentRepository.save(incident);
    }
    
    public void deleteIncident(Long id) {
        if (!incidentRepository.existsById(id)) {
            throw new RuntimeException("Traffic incident not found with id: " + id);
        }
        incidentRepository.deleteById(id);
    }
}
// package com.example.demo.service;

// import com.example.demo.dto.IncidentDto;
// import com.example.demo.entity.*;
// import com.example.demo.repository.AlertNotificationRepository;
// import com.example.demo.repository.TrafficIncidentRepository;
// import com.example.demo.repository.TrafficZoneRepository;
// import com.example.demo.repository.CityUserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDateTime;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// @Transactional
// public class TrafficIncidentService {
    
//     private final TrafficIncidentRepository incidentRepository;
//     private final TrafficZoneRepository zoneRepository;
//     private final CityUserRepository userRepository;
//     private final AlertNotificationRepository alertRepository;
    
//     public TrafficIncident reportIncident(IncidentDto dto, String username) {
//         TrafficZone zone = zoneRepository.findById(dto.getZoneId())
//             .orElseThrow(() -> new RuntimeException("Traffic zone not found with id: " + dto.getZoneId()));
        
//         CityUser reporter = userRepository.findByUsername(username)
//             .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
//         TrafficIncident incident = new TrafficIncident();
//         incident.setTitle(dto.getTitle());
//         incident.setIncidentType(TrafficIncident.IncidentType.valueOf(dto.getIncidentType()));
//         incident.setSeverity(TrafficIncident.Severity.valueOf(dto.getSeverity()));
//         incident.setZone(zone);
//         incident.setReportedBy(reporter);
//         incident.setStatus(TrafficIncident.IncidentStatus.REPORTED);
//         incident.setReportedAt(LocalDateTime.now());
//         incident.setDescription(dto.getDescription());
        
//         TrafficIncident savedIncident = incidentRepository.save(incident);
        
//         if (savedIncident.getSeverity() == TrafficIncident.Severity.HIGH || 
//             savedIncident.getSeverity() == TrafficIncident.Severity.CRITICAL) {
            
//             AlertNotification alert = new AlertNotification();
//             alert.setTargetRole(Role.TRAFFIC_CONTROLLER);
//             alert.setMessage("High severity incident reported: " + savedIncident.getTitle());
//             alert.setRelatedEntityType("TrafficIncident");
//             alert.setRelatedEntityId(savedIncident.getIncidentId());
//             alert.setSeverity(AlertNotification.Severity.valueOf(savedIncident.getSeverity().name()));
            
//             alertRepository.save(alert);
//         }
        
//         return savedIncident;
//     }
    
//     public TrafficIncident dispatchResponse(Long id) {
//         TrafficIncident incident = incidentRepository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Traffic incident not found with id: " + id));
        
//         if (incident.getStatus() != TrafficIncident.IncidentStatus.REPORTED) {
//             throw new IllegalStateException("Incident must be in REPORTED state to dispatch. Current status: " + incident.getStatus());
//         }
        
//         incident.setStatus(TrafficIncident.IncidentStatus.DISPATCHED);
//         return incidentRepository.save(incident);
//     }
    
//     public List<TrafficIncident> getAllIncidents() {
//         return incidentRepository.findAll();
//     }
    
//     public TrafficIncident getIncidentById(Long id) {
//         return incidentRepository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Traffic incident not found with id: " + id));
//     }
    
//     public TrafficIncident updateIncident(Long id, IncidentDto dto) {
//         TrafficIncident incident = incidentRepository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Traffic incident not found with id: " + id));
        
//         TrafficZone zone = zoneRepository.findById(dto.getZoneId())
//             .orElseThrow(() -> new RuntimeException("Traffic zone not found with id: " + dto.getZoneId()));
        
//         incident.setTitle(dto.getTitle());
//         incident.setIncidentType(TrafficIncident.IncidentType.valueOf(dto.getIncidentType()));
//         incident.setSeverity(TrafficIncident.Severity.valueOf(dto.getSeverity()));
//         incident.setZone(zone);
//         incident.setDescription(dto.getDescription());
        
//         return incidentRepository.save(incident);
//     }
    
//     public void deleteIncident(Long id) {
//         if (!incidentRepository.existsById(id)) {
//             throw new RuntimeException("Traffic incident not found with id: " + id);
//         }
//         incidentRepository.deleteById(id);
//     }
// }