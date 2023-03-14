package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lane_address", nullable = false, length = 30)
    private String laneAddress;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "pin_code", nullable = false, length = 30)
    private String pinCode;

    @Column(name = "state", nullable = false, length = 30)
    private String state;

    @Column(name = "country", nullable = false, length = 30)
    private String country;

    @Column(name = "address_status",nullable = false)
    private Character isActive = 'Y';
}
