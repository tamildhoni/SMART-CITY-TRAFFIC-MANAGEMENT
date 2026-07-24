package com.example.demo.controller;

import com.example.demo.entity.TrafficIncident;
import com.example.demo.entity.TrafficZone;
import com.example.demo.entity.CityUser;
import com.example.demo.entity.Role;
import com.example.demo.service.TrafficIncidentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrafficIncidentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrafficIncidentService trafficIncidentService;

    @InjectMocks
    private TrafficIncidentController trafficIncidentController;

    private ObjectMapper objectMapper;
    private TrafficIncident incident1;
    private TrafficIncident incident2;
    private TrafficZone zone;
    private CityUser reporter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trafficIncidentController).build();
        objectMapper = new ObjectMapper();

        // Create test data
        zone = new TrafficZone();
        zone.setZoneId(1L);
        zone.setZoneName("Downtown");
        zone.setZoneCode("DT001");

        reporter = new CityUser();
        reporter.setUserId(1L);
        reporter.setUsername("testUser");
        reporter.setRole(Role.TRAFFIC_CONTROLLER);

        incident1 = new TrafficIncident();
        incident1.setIncidentId(1L);
        incident1.setTitle("Accident on Main St");
        incident1.setIncidentType(TrafficIncident.IncidentType.ACCIDENT);
        incident1.setSeverity(TrafficIncident.Severity.HIGH);
        incident1.setStatus(TrafficIncident.IncidentStatus.REPORTED);
        incident1.setZone(zone);
        incident1.setReportedBy(reporter);
        incident1.setReportedAt(LocalDateTime.now());

        incident2 = new TrafficIncident();
        incident2.setIncidentId(2L);
        incident2.setTitle("Traffic Jam on Highway");
        incident2.setIncidentType(TrafficIncident.IncidentType.TRAFFIC_JAM);
        incident2.setSeverity(TrafficIncident.Severity.MEDIUM);
        incident2.setStatus(TrafficIncident.IncidentStatus.DISPATCHED);
        incident2.setZone(zone);
        incident2.setReportedBy(reporter);
        incident2.setReportedAt(LocalDateTime.now());
    }

    // ============ T7: GET ALL ENDPOINT TESTS ============

    @Test
    void testGetAllIncidents_ShouldReturnListOfIncidents() throws Exception {
        List<TrafficIncident> incidents = Arrays.asList(incident1, incident2);
        when(trafficIncidentService.getAllIncidents()).thenReturn(incidents);

        mockMvc.perform(get("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].incidentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Accident on Main St"))
                .andExpect(jsonPath("$[1].incidentId").value(2L))
                .andExpect(jsonPath("$[1].title").value("Traffic Jam on Highway"));
    }

    @Test
    void testGetAllIncidents_ShouldReturnEmptyListWhenNoIncidents() throws Exception {
        when(trafficIncidentService.getAllIncidents()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ============ T9: GET BY ID ENDPOINT TESTS ============

    @Test
    void testGetIncidentById_ShouldReturnIncident() throws Exception {
        Long incidentId = 1L;
        when(trafficIncidentService.getIncidentById(incidentId)).thenReturn(incident1);

        mockMvc.perform(get("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentId").value(1L))
                .andExpect(jsonPath("$.title").value("Accident on Main St"))
                .andExpect(jsonPath("$.incidentType").value("ACCIDENT"))
                .andExpect(jsonPath("$.severity").value("HIGH"))
                .andExpect(jsonPath("$.status").value("REPORTED"));
    }

    @Test
    void testGetIncidentById_WithNonExistentId_ShouldThrowException() throws Exception {
        Long incidentId = 999L;
        when(trafficIncidentService.getIncidentById(incidentId))
                .thenThrow(new RuntimeException("Traffic incident not found with id: " + incidentId));

        mockMvc.perform(get("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    // ============ T8: DELETE ENDPOINT TESTS ============

    @Test
    void testDeleteIncident_ShouldReturnSuccessMessage() throws Exception {
        Long incidentId = 1L;
        String expectedMessage = "TrafficIncident deleted successfully.";
        
        org.mockito.Mockito.doNothing().when(trafficIncidentService).deleteIncident(incidentId);

        mockMvc.perform(delete("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    void testDeleteIncident_WithNonExistentId_ShouldThrowException() throws Exception {
        Long incidentId = 999L;
        org.mockito.Mockito.doThrow(new RuntimeException("Traffic incident not found with id: " + incidentId))
                .when(trafficIncidentService).deleteIncident(incidentId);

        mockMvc.perform(delete("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}