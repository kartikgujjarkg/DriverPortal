package com.example.demo.service;

import com.example.demo.entity.Driver;

public interface DriverService {

    public Driver registerDriver(Driver driver);

    public Driver getDriver(Long driverId);

    public Driver updateDriver(Driver driver, Long driverId);

    public Driver markDriverReady(Long driverId);

    public Driver deleteDriver(Long driverId);
}
