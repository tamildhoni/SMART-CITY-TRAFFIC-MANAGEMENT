package com.example.demo.controller;

import com.example.demo.dto.IncidentDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private IncidentDto incidentDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trafficIncidentController).build();
        objectMapper = new ObjectMapper();

        // Create test data
        zone = new TrafficZone();
        zone.setZoneId(1L);
        zone.setZoneName("Downtown");
        zone.setZoneCode("DT001");
        zone.setDistrict("Central");
        zone.setCurrentCongestionLevel(TrafficZone.CongestionLevel.HIGH);
        zone.setSignalCycleSeconds(120);

        reporter = new CityUser();
        reporter.setUserId(1L);
        reporter.setUsername("testUser");
        reporter.setFullName("Test User");
        reporter.setRole(Role.TRAFFIC_CONTROLLER);
        reporter.setDistrict("Central");
        reporter.setBadgeNumber("TC001");
        reporter.setActive(true);

        incident1 = new TrafficIncident();
        incident1.setIncidentId(1L);
        incident1.setTitle("Accident on Main St");
        incident1.setIncidentType(TrafficIncident.IncidentType.ACCIDENT);
        incident1.setSeverity(TrafficIncident.Severity.HIGH);
        incident1.setStatus(TrafficIncident.IncidentStatus.REPORTED);
        incident1.setZone(zone);
        incident1.setReportedBy(reporter);
        incident1.setReportedAt(LocalDateTime.now());
        incident1.setDescription("Major accident at intersection");

        incident2 = new TrafficIncident();
        incident2.setIncidentId(2L);
        incident2.setTitle("Traffic Jam on Highway");
        incident2.setIncidentType(TrafficIncident.IncidentType.TRAFFIC_JAM);
        incident2.setSeverity(TrafficIncident.Severity.MEDIUM);
        incident2.setStatus(TrafficIncident.IncidentStatus.DISPATCHED);
        incident2.setZone(zone);
        incident2.setReportedBy(reporter);
        incident2.setReportedAt(LocalDateTime.now().minusHours(1));
        incident2.setDescription("Heavy traffic due to construction");

        incidentDto = new IncidentDto();
        incidentDto.setTitle("New Accident Reported");
        incidentDto.setIncidentType("ACCIDENT");
        incidentDto.setSeverity("HIGH");
        incidentDto.setZoneId(1L);
        incidentDto.setDescription("Description of the incident");
    }

    // ============ T7: GET ALL ENDPOINT TESTS ============

    @Test
    void testGetAllIncidents_ShouldReturnListOfIncidents() throws Exception {
        // Arrange
        List<TrafficIncident> incidents = Arrays.asList(incident1, incident2);
        when(trafficIncidentService.getAllIncidents()).thenReturn(incidents);

        // Act & Assert
        mockMvc.perform(get("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].incidentId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Accident on Main St"))
                .andExpect(jsonPath("$[0].incidentType").value("ACCIDENT"))
                .andExpect(jsonPath("$[0].severity").value("HIGH"))
                .andExpect(jsonPath("$[0].status").value("REPORTED"))
                .andExpect(jsonPath("$[1].incidentId").value(2L))
                .andExpect(jsonPath("$[1].title").value("Traffic Jam on Highway"))
                .andExpect(jsonPath("$[1].incidentType").value("TRAFFIC_JAM"))
                .andExpect(jsonPath("$[1].severity").value("MEDIUM"))
                .andExpect(jsonPath("$[1].status").value("DISPATCHED"));
    }

    @Test
    void testGetAllIncidents_ShouldReturnEmptyListWhenNoIncidents() throws Exception {
        // Arrange
        when(trafficIncidentService.getAllIncidents()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAllIncidents_ShouldReturnCorrectResponseContentType() throws Exception {
        // Arrange
        List<TrafficIncident> incidents = Arrays.asList(incident1);
        when(trafficIncidentService.getAllIncidents()).thenReturn(incidents);

        // Act & Assert
        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ============ T9: GET BY ID ENDPOINT TESTS ============

    @Test
    void testGetIncidentById_ShouldReturnIncident() throws Exception {
        // Arrange
        Long incidentId = 1L;
        when(trafficIncidentService.getIncidentById(incidentId)).thenReturn(incident1);

        // Act & Assert
        mockMvc.perform(get("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentId").value(1L))
                .andExpect(jsonPath("$.title").value("Accident on Main St"))
                .andExpect(jsonPath("$.incidentType").value("ACCIDENT"))
                .andExpect(jsonPath("$.severity").value("HIGH"))
                .andExpect(jsonPath("$.status").value("REPORTED"))
                .andExpect(jsonPath("$.zone.zoneId").value(1L))
                .andExpect(jsonPath("$.zone.zoneName").value("Downtown"))
                .andExpect(jsonPath("$.reportedBy.username").value("testUser"));
    }

    @Test
    void testGetIncidentById_WithNonExistentId_ShouldThrowException() throws Exception {
        // Arrange
        Long incidentId = 999L;
        when(trafficIncidentService.getIncidentById(incidentId))
                .thenThrow(new RuntimeException("Traffic incident not found with id: " + incidentId));

        // Act & Assert
        mockMvc.perform(get("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetIncidentById_ShouldReturnCorrectIncidentDetails() throws Exception {
        // Arrange
        Long incidentId = 2L;
        when(trafficIncidentService.getIncidentById(incidentId)).thenReturn(incident2);

        // Act & Assert
        mockMvc.perform(get("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentId").value(2L))
                .andExpect(jsonPath("$.title").value("Traffic Jam on Highway"))
                .andExpect(jsonPath("$.incidentType").value("TRAFFIC_JAM"))
                .andExpect(jsonPath("$.severity").value("MEDIUM"))
                .andExpect(jsonPath("$.status").value("DISPATCHED"));
    }

    @Test
    void testGetIncidentById_WithInvalidIdFormat_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/incidents/{id}", "invalid-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetIncidentById_ShouldReturnResponseContentTypeJson() throws Exception {
        // Arrange
        Long incidentId = 1L;
        when(trafficIncidentService.getIncidentById(incidentId)).thenReturn(incident1);

        // Act & Assert
        mockMvc.perform(get("/api/incidents/{id}", incidentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ============ T8: DELETE ENDPOINT TESTS ============

    @Test
    void testDeleteIncident_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        Long incidentId = 1L;
        String expectedMessage = "TrafficIncident deleted successfully.";
        
        // Mock the service method (void return)
        doNothing().when(trafficIncidentService).deleteIncident(incidentId);

        // Act & Assert
        mockMvc.perform(delete("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    void testDeleteIncident_WithNonExistentId_ShouldThrowException() throws Exception {
        // Arrange
        Long incidentId = 999L;
        doThrow(new RuntimeException("Traffic incident not found with id: " + incidentId))
                .when(trafficIncidentService).deleteIncident(incidentId);

        // Act & Assert
        mockMvc.perform(delete("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteIncident_WithInvalidIdFormat_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Passing non-numeric ID
        mockMvc.perform(delete("/api/incidents/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteIncident_WithNegativeId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert - Passing negative ID
        mockMvc.perform(delete("/api/incidents/{id}", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ============ POST ENDPOINT TESTS (Create Incident) ============

    @Test
    void testCreateIncident_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        when(trafficIncidentService.reportIncident(any(IncidentDto.class), any(String.class)))
                .thenReturn(incident1);

        // Act & Assert
        mockMvc.perform(post("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidentDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("TrafficIncident created successfully."));
    }

    @Test
    void testCreateIncident_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange - Create invalid DTO (missing required fields)
        IncidentDto invalidDto = new IncidentDto();
        invalidDto.setTitle(""); // Empty title - validation should fail
        invalidDto.setZoneId(null); // Null zoneId - validation should fail

        // Act & Assert
        mockMvc.perform(post("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    // ============ PUT ENDPOINT TESTS (Update Incident) ============

    @Test
    void testUpdateIncident_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        Long incidentId = 1L;
        when(trafficIncidentService.updateIncident(eq(incidentId), any(IncidentDto.class)))
                .thenReturn(incident1);

        // Act & Assert
        mockMvc.perform(put("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidentDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("TrafficIncident updated successfully."));
    }

    @Test
    void testUpdateIncident_WithNonExistentId_ShouldThrowException() throws Exception {
        // Arrange
        Long incidentId = 999L;
        when(trafficIncidentService.updateIncident(eq(incidentId), any(IncidentDto.class)))
                .thenThrow(new RuntimeException("Traffic incident not found with id: " + incidentId));

        // Act & Assert
        mockMvc.perform(put("/api/incidents/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incidentDto)))
                .andExpect(status().isInternalServerError());
    }

    // ============ DISPATCH ENDPOINT TESTS ============

    @Test
    void testDispatchIncident_ShouldReturnUpdatedIncident() throws Exception {
        // Arrange
        Long incidentId = 1L;
        TrafficIncident dispatchedIncident = new TrafficIncident();
        dispatchedIncident.setIncidentId(1L);
        dispatchedIncident.setTitle("Accident on Main St");
        dispatchedIncident.setStatus(TrafficIncident.IncidentStatus.DISPATCHED);
        dispatchedIncident.setZone(zone);
        dispatchedIncident.setReportedBy(reporter);
        
        when(trafficIncidentService.dispatchResponse(incidentId)).thenReturn(dispatchedIncident);

        // Act & Assert
        mockMvc.perform(put("/api/incidents/{id}/dispatch", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentId").value(1L))
                .andExpect(jsonPath("$.status").value("DISPATCHED"));
    }

    @Test
    void testDispatchIncident_WithInvalidState_ShouldThrowException() throws Exception {
        // Arrange
        Long incidentId = 1L;
        when(trafficIncidentService.dispatchResponse(incidentId))
                .thenThrow(new IllegalStateException("Incident must be in REPORTED state to dispatch"));

        // Act & Assert
        mockMvc.perform(put("/api/incidents/{id}/dispatch", incidentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    // ============ EDGE CASE TESTS ============

    @Test
    void testGetAllIncidents_WhenServiceThrowsException_ShouldReturnError() throws Exception {
        // Arrange
        when(trafficIncidentService.getAllIncidents())
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        mockMvc.perform(get("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetIncidentById_WithNullId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/incidents/{id}", (Object) null)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteIncident_WithNullId_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/incidents/{id}", (Object) null)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}