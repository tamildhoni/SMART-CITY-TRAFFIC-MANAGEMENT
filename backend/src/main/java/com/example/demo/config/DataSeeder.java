package com.example.demo.config;

import com.example.demo.entity.CityUser;
import com.example.demo.entity.Role;
import com.example.demo.repository.CityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CityUserRepository cityUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (cityUserRepository.count() == 0) {

            CityUser admin = new CityUser();

            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFullName("City Administrator");
            admin.setDistrict("Coimbatore");
            admin.setBadgeNumber("ADM001");
            admin.setRole(Role.CITY_ADMINISTRATOR);
            admin.setIsActive(true);

            cityUserRepository.save(admin);

            CityUser controller = new CityUser();

            controller.setUsername("traffic");
            controller.setPasswordHash(passwordEncoder.encode("traffic123"));
            controller.setFullName("Traffic Controller");
            controller.setDistrict("Coimbatore");
            controller.setBadgeNumber("TRF001");
            controller.setRole(Role.TRAFFIC_CONTROLLER);
            controller.setIsActive(true);

            cityUserRepository.save(controller);

            System.out.println("Default users inserted successfully.");
        }
    }
}