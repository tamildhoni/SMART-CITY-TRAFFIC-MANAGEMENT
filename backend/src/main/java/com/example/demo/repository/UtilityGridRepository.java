package com.example.demo.repository;

import com.example.demo.entity.UtilityGrid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilityGridRepository extends JpaRepository<UtilityGrid, Long> {
    List<UtilityGrid> findByDistrict(String district);
    Optional<UtilityGrid> findByGridId(Long gridId);
}