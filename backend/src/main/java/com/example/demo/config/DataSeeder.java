package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    
    private final CityUserRepository userRepository;
    private final TrafficZoneRepository zoneRepository;
    private final UtilityGridRepository gridRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Seed Users
        if (userRepository.count() == 0) {
            // Admin
            CityUser admin = new CityUser();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setRole(Role.CITY_ADMINISTRATOR);
            admin.setDistrict("Central");
            admin.setBadgeNumber("ADMIN001");
            admin.setActive(true);
            userRepository.save(admin);
            
            // Traffic Controller
            CityUser controller = new CityUser();
            controller.setUsername("controller");
            controller.setPasswordHash(passwordEncoder.encode("controller123"));
            controller.setFullName("Traffic Controller");
            controller.setRole(Role.TRAFFIC_CONTROLLER);
            controller.setDistrict("North");
            controller.setBadgeNumber("TC001");
            controller.setActive(true);
            userRepository.save(controller);
            
            // Utility Supervisor
            CityUser supervisor = new CityUser();
            supervisor.setUsername("supervisor");
            supervisor.setPasswordHash(passwordEncoder.encode("supervisor123"));
            supervisor.setFullName("Utility Supervisor");
            supervisor.setRole(Role.UTILITY_SUPERVISOR);
            supervisor.setDistrict("South");
            supervisor.setBadgeNumber("US001");
            supervisor.setActive(true);
            userRepository.save(supervisor);
        }
        
        // Seed Traffic Zones
        if (zoneRepository.count() == 0) {
            TrafficZone zone1 = new TrafficZone();
            zone1.setZoneName("Downtown");
            zone1.setZoneCode("DT001");
            zone1.setDistrict("Central");
            zone1.setCurrentCongestionLevel(TrafficZone.CongestionLevel.HIGH);
            zone1.setSignalCycleSeconds(120);
            zoneRepository.save(zone1);
            
            TrafficZone zone2 = new TrafficZone();
            zone2.setZoneName("Industrial Area");
            zone2.setZoneCode("IA001");
            zone2.setDistrict("North");
            zone2.setCurrentCongestionLevel(TrafficZone.CongestionLevel.MODERATE);
            zone2.setSignalCycleSeconds(90);
            zoneRepository.save(zone2);
        }
        
        // Seed Utility Grids
        if (gridRepository.count() == 0) {
            UtilityGrid grid1 = new UtilityGrid();
            grid1.setGridName("Central Power Grid");
            grid1.setGridType(UtilityGrid.GridType.ELECTRICITY);
            grid1.setDistrict("Central");
            grid1.setCapacityUnits(1000.0);
            grid1.setCurrentLoad(650.0);
            grid1.setStatus(UtilityGrid.GridStatus.OPERATIONAL);
            gridRepository.save(grid1);
        }
    }
}