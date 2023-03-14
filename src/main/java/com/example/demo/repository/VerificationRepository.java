package com.example.demo.repository;

import com.example.demo.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    @Query(
    value = "SELECT * FROM verification WHERE driver_id = ?1",
    nativeQuery = true)
    Set<Verification> findByDriverId(Long driverId);

    @Query(
            value = "SELECT * FROM verification WHERE driver_id = ?1 and verification_status != 'APPROVED'",
            nativeQuery = true)
    Set<Verification> findPendingTasksForDriverId(Long driverId);

}
