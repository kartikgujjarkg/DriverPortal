package com.example.demo.controller;

import com.example.demo.entity.Driver;
import com.example.demo.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uber")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping(value = "/register/driver")
    public ResponseEntity<Driver> registerDriver(@RequestBody Driver driver){
        Driver createdDriver = driverService.registerDriver(driver);
        return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
    }

    @GetMapping(value = "/driver/{driverId}")
    public ResponseEntity<Driver> getDriver(@PathVariable("driverId") Long driverId){
        Driver driver = driverService.getDriver(driverId);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @PutMapping(value = "/driver/{driverId}")
    public ResponseEntity<Driver> updateDriver(@RequestBody Driver driver,@PathVariable("driverId") Long driverId){
        Driver updatedDriver = driverService.updateDriver(driver,driverId);
        return new ResponseEntity<>(updatedDriver, HttpStatus.OK);
    }

    @DeleteMapping(value="/driver/{driverId}")
    public ResponseEntity<Long> deleteDriver(@PathVariable("driverId") Long driverId){
        driverService.deleteDriver(driverId);
        return new ResponseEntity<>(driverId, HttpStatus.OK);
    }

    @PatchMapping(value = "/mark-ready/driver/{driverId}")
    public ResponseEntity<Driver> markDriverReady(@PathVariable("driverId") Long driverId){
        Driver driver = driverService.markDriverReady(driverId);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

}
