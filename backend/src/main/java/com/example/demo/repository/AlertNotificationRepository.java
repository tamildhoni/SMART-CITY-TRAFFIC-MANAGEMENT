package com.example.demo.repository;

import com.example.demo.entity.AlertNotification;
import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertNotificationRepository extends JpaRepository<AlertNotification, Long> {
    List<AlertNotification> findByTargetRole(Role role);
    List<AlertNotification> findByTargetRoleAndIsReadFalse(Role role);
}