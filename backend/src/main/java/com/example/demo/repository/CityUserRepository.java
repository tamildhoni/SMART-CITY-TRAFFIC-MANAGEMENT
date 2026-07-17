package com.example.demo.repository;

import com.example.demo.entity.CityUser;
import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityUserRepository extends JpaRepository<CityUser, Long> {
    
    Optional<CityUser> findByUsername(String username);
    
    List<CityUser> findByRole(Role role);
    
    boolean existsByUsername(String username);
}