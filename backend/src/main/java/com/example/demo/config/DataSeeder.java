package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {
    
    @Bean
    public CommandLineRunner seedData(CityUserRepository userRepository,
                                      TrafficZoneRepository zoneRepository,
                                      TrafficIncidentRepository incidentRepository,
                                      UtilityGridRepository gridRepository,
                                      UtilityOutageRepository outageRepository,
                                      AlertNotificationRepository alertRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed only if database is empty
            if (userRepository.count() == 0) {
                
                // 1. Create Admin User
                CityUser admin = new CityUser();
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setFullName("System Administrator");
                admin.setRole(Role.CITY_ADMINISTRATOR);
                admin.setDistrict("Central");
                admin.setBadgeNumber("ADMIN001");
                admin.setActive(true);
                userRepository.save(admin);
                
                // 2. Create Traffic Controller
                CityUser controller = new CityUser();
                controller.setUsername("traffic_controller");
                controller.setPasswordHash(passwordEncoder.encode("controller123"));
                controller.setFullName("John Traffic");
                controller.setRole(Role.TRAFFIC_CONTROLLER);
                controller.setDistrict("Downtown");
                controller.setBadgeNumber("TC001");
                controller.setActive(true);
                userRepository.save(controller);
                
                // 3. Create Utility Supervisor
                CityUser supervisor = new CityUser();
                supervisor.setUsername("utility_supervisor");
                supervisor.setPasswordHash(passwordEncoder.encode("supervisor123"));
                supervisor.setFullName("Jane Utility");
                supervisor.setRole(Role.UTILITY_SUPERVISOR);
                supervisor.setDistrict("Industrial");
                supervisor.setBadgeNumber("US001");
                supervisor.setActive(true);
                userRepository.save(supervisor);
                
                // 4. Create Field Technician
                CityUser technician = new CityUser();
                technician.setUsername("field_tech");
                technician.setPasswordHash(passwordEncoder.encode("tech123"));
                technician.setFullName("Mike Field");
                technician.setRole(Role.FIELD_TECHNICIAN);
                technician.setDistrict("Suburbs");
                technician.setBadgeNumber("FT001");
                technician.setActive(true);
                userRepository.save(technician);
                
                // 5. Create Traffic Zones
                TrafficZone zone1 = new TrafficZone();
                zone1.setZoneName("Downtown Core");
                zone1.setZoneCode("ZONE-001");
                zone1.setDistrict("Central");
                zone1.setCurrentCongestionLevel(CongestionLevel.HIGH);
                zone1.setSignalCycleSeconds(90);
                zoneRepository.save(zone1);
                
                TrafficZone zone2 = new TrafficZone();
                zone2.setZoneName("Industrial Area");
                zone2.setZoneCode("ZONE-002");
                zone2.setDistrict("Industrial");
                zone2.setCurrentCongestionLevel(CongestionLevel.MODERATE);
                zone2.setSignalCycleSeconds(75);
                zoneRepository.save(zone2);
                
                TrafficZone zone3 = new TrafficZone();
                zone3.setZoneName("Residential North");
                zone3.setZoneCode("ZONE-003");
                zone3.setDistrict("North");
                zone3.setCurrentCongestionLevel(CongestionLevel.LOW);
                zone3.setSignalCycleSeconds(60);
                zoneRepository.save(zone3);
                
                // 6. Create Traffic Incidents
                TrafficIncident incident1 = new TrafficIncident();
                incident1.setTitle("Major Accident on Main Street");
                incident1.setIncidentType(IncidentType.ACCIDENT);
                incident1.setSeverity(Severity.HIGH);
                incident1.setStatus(IncidentStatus.REPORTED);
                incident1.setZone(zone1);
                incident1.setReportedBy(controller);
                incident1.setDescription("Multi-vehicle collision blocking both lanes");
                incidentRepository.save(incident1);
                
                TrafficIncident incident2 = new TrafficIncident();
                incident2.setTitle("Construction on Highway 101");
                incident2.setIncidentType(IncidentType.CONSTRUCTION);
                incident2.setSeverity(Severity.MEDIUM);
                incident2.setStatus(IncidentStatus.DISPATCHED);
                incident2.setZone(zone2);
                incident2.setReportedBy(controller);
                incident2.setDescription("Road construction causing lane closures");
                incidentRepository.save(incident2);
                
                // 7. Create Utility Grids
                UtilityGrid grid1 = new UtilityGrid();
                grid1.setGridName("Power Grid Central");
                grid1.setGridType(GridType.ELECTRICITY);
                grid1.setDistrict("Central");
                grid1.setCapacityUnits(1000.0);
                grid1.setCurrentLoad(650.0);
                grid1.setStatus(GridStatus.OPERATIONAL);
                gridRepository.save(grid1);
                
                UtilityGrid grid2 = new UtilityGrid();
                grid2.setGridName("Water Distribution North");
                grid2.setGridType(GridType.WATER);
                grid2.setDistrict("North");
                grid2.setCapacityUnits(500.0);
                grid2.setCurrentLoad(480.0);
                grid2.setStatus(GridStatus.DEGRADED);
                gridRepository.save(grid2);
                
                // 8. Create Utility Outages
                UtilityOutage outage1 = new UtilityOutage();
                outage1.setGrid(grid2);
                outage1.setOutageType(OutageType.UNPLANNED);
                outage1.setAffectedArea("North Residential");
                outage1.setSeverity(Severity.HIGH);
                outage1.setStatus(OutageStatus.ACTIVE);
                outage1.setDescription("Water main break affecting 500 households");
                outageRepository.save(outage1);
                
                // 9. Create Alert Notifications
                AlertNotification alert1 = new AlertNotification();
                alert1.setTargetRole(Role.CITY_ADMINISTRATOR);
                alert1.setMessage("High severity incident reported: Major Accident on Main Street");
                alert1.setRelatedEntityType("TrafficIncident");
                alert1.setRelatedEntityId(incident1.getIncidentId());
                alert1.setSeverity(Severity.HIGH);
                alert1.setRead(false);
                alertRepository.save(alert1);
                
                AlertNotification alert2 = new AlertNotification();
                alert2.setTargetRole(Role.UTILITY_SUPERVISOR);
                alert2.setMessage("Utility grid DEGRADED: Water Distribution North");
                alert2.setRelatedEntityType("UtilityGrid");
                alert2.setRelatedEntityId(grid2.getGridId());
                alert2.setSeverity(Severity.MEDIUM);
                alert2.setRead(false);
                alertRepository.save(alert2);
                
                System.out.println("Data seeding completed successfully!");
            }
        };
    }
}