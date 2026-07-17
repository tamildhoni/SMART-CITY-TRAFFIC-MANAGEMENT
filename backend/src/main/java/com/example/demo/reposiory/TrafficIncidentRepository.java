package com.example.demo.repository;

import com.example.demo.entity.TrafficIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrafficIncidentRepository extends JpaRepository<TrafficIncident, Long> {
    
    @Query("SELECT ti FROM TrafficIncident ti WHERE ti.zone.zoneId = :zoneId AND ti.status NOT IN :statuses")
    List<TrafficIncident> findByZone_ZoneIdAndStatusNotIn(@Param("zoneId") Long zoneId, 
                                                           @Param("statuses") List<TrafficIncident.IncidentStatus> statuses);
    
    long countByStatusNotIn(List<TrafficIncident.IncidentStatus> statuses);
    
    @Query("SELECT ti.incidentType, COUNT(ti) FROM TrafficIncident ti GROUP BY ti.incidentType")
    List<Object[]> countIncidentsByType();
}