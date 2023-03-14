package com.example.demo.repository;

import com.example.demo.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query(value = "SELECT * FROM Driver d WHERE d.phone = ?1 AND d.is_active = 'Y'",nativeQuery = true)
    public Driver findByPhone(String phone);

    @Query(value= "SELECT * FROM Driver d WHERE d.id = ?1 AND d.is_active = 'Y' ", nativeQuery = true)
    public Optional<Driver> findById(Long id);

}
