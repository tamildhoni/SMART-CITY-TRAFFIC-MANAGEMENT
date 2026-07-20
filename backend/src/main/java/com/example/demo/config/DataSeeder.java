package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            if (userRepository.count() == 0) {
                
                // Create Admin User
                CityUser admin = new CityUser();
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setFullName("System Administrator");
                admin.setRole(Role.CITY_ADMINISTRATOR);
                admin.setDistrict("Central");
                admin.setBadgeNumber("ADMIN001");
                admin.setActive(true);
                userRepository.save(admin);
                
                // Create Traffic Controller
                CityUser controller = new CityUser();
                controller.setUsername("traffic_controller");
                controller.setPasswordHash(passwordEncoder.encode("controller123"));
                controller.setFullName("John Traffic");
                controller.setRole(Role.TRAFFIC_CONTROLLER);
                controller.setDistrict("Downtown");
                controller.setBadgeNumber("TC001");
                controller.setActive(true);
                userRepository.save(controller);
                
                // Create Utility Supervisor
                CityUser supervisor = new CityUser();
                supervisor.setUsername("utility_supervisor");
                supervisor.setPasswordHash(passwordEncoder.encode("supervisor123"));
                supervisor.setFullName("Jane Utility");
                supervisor.setRole(Role.UTILITY_SUPERVISOR);
                supervisor.setDistrict("Industrial");
                supervisor.setBadgeNumber("US001");
                supervisor.setActive(true);
                userRepository.save(supervisor);
                
                // Create Traffic Zones
                TrafficZone zone1 = new TrafficZone();
                zone1.setZoneName("Downtown Core");
                zone1.setZoneCode("ZONE-001");
                zone1.setDistrict("Central");
                zone1.setCurrentCongestionLevel(TrafficZone.CongestionLevel.HIGH);
                zone1.setSignalCycleSeconds(90);
                zoneRepository.save(zone1);
                
                TrafficZone zone2 = new TrafficZone();
                zone2.setZoneName("Industrial Area");
                zone2.setZoneCode("ZONE-002");
                zone2.setDistrict("Industrial");
                zone2.setCurrentCongestionLevel(TrafficZone.CongestionLevel.MODERATE);
                zone2.setSignalCycleSeconds(75);
                zoneRepository.save(zone2);
                
                // Create Utility Grids
                UtilityGrid grid1 = new UtilityGrid();
                grid1.setGridName("Power Grid Central");
                grid1.setGridType(UtilityGrid.GridType.ELECTRICITY);
                grid1.setDistrict("Central");
                grid1.setCapacityUnits(1000.0);
                grid1.setCurrentLoad(650.0);
                grid1.setStatus(UtilityGrid.GridStatus.OPERATIONAL);
                gridRepository.save(grid1);
                
                UtilityGrid grid2 = new UtilityGrid();
                grid2.setGridName("Water Distribution North");
                grid2.setGridType(UtilityGrid.GridType.WATER);
                grid2.setDistrict("North");
                grid2.setCapacityUnits(500.0);
                grid2.setCurrentLoad(480.0);
                grid2.setStatus(UtilityGrid.GridStatus.DEGRADED);
                gridRepository.save(grid2);
                
                System.out.println("Data seeding completed successfully!");
            }
        };
    }
}