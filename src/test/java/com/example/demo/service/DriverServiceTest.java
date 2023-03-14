package com.example.demo.service;


import com.example.demo.entity.Driver;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.Verification;
import com.example.demo.enums.ReadyStatus;
import com.example.demo.enums.VerificationStatus;
import com.example.demo.enums.VerificationType;
import com.example.demo.exception.DriverAlreadyPresentException;
import com.example.demo.exception.DriverNotFoundException;
import com.example.demo.exception.VerificationPendingException;
import com.example.demo.repository.DriverRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class DriverServiceTest {

    @Autowired
    DriverService driverService;

    @MockBean
    private DriverRepository driverRepository;

    @Test
    public void registerDriverHappyFlowTest(){
        Mockito.when(driverRepository.save(any(Driver.class))).thenReturn(createValidDriverResponse());
        Driver driver = driverService.registerDriver(createValidDriverInput());
        Assert.assertEquals(driver.getPhone(),"9123456780");
    }

    @Test
    public void registerSameDriverAgain(){
        Mockito.when(driverRepository.findByPhone((any(String.class)))).thenReturn(createValidDriverResponse());
        Assert.assertThrows(DriverAlreadyPresentException.class,()->driverService.registerDriver(createValidDriverInput()));
    }

    @Test
    public void getDriverHappyFlow(){
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(createValidDriverResponse()));
        Driver driver = driverService.getDriver(1L);
        Assert.assertEquals(driver.getPhone(),"9123456780");
    }

    @Test
    public void getUnregisteredDriver(){
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assert.assertThrows(DriverNotFoundException.class,()->driverService.getDriver(4L));
    }


    @Test
    public void updateDriverHappyFlow(){
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(createValidDriverResponse()));
        Mockito.when(driverRepository.save(any(Driver.class))).thenReturn(createUpdatedDriverResponse());
        Driver updatedDriverPayload = createUpdatedDriverPayload();
        Driver driver = driverService.updateDriver(updatedDriverPayload,createValidDriverResponse().getId());
        Assert.assertEquals(driver.getFirstName(),createUpdatedDriverPayload().getFirstName());
        Assert.assertEquals(driver.getLastName(),createUpdatedDriverPayload().getLastName());
    }

    @Test
    public void updateNonExistingDriver(){
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Driver updatedDriverPayload = createUpdatedDriverPayload();
        Assert.assertThrows(DriverNotFoundException.class,()->driverService.updateDriver(updatedDriverPayload,createValidDriverResponse().getId()));
    }


    @Test
    public void deleteUserHappyFlow(){
        Driver driver = createValidDriverResponse();
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(driver));
        Mockito.when((driverRepository.save(any(Driver.class)))).thenReturn(createDeletedDriverResponse());
        Driver deletedDriver = driverService.deleteDriver(driver.getId());
        Assert.assertEquals(deletedDriver.getIsActive().charValue(),'N');
    }


    @Test
    public void deleteNonExistingUser(){
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assert.assertThrows(DriverNotFoundException.class,()->driverService.deleteDriver(createValidDriverResponse().getId()));
    }

    @Test
    public void markDriverAsReadyHappyFlow(){
        Driver driver = createValidDriverResponseWithAllVerificationsCompleted();
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(driver));
        Mockito.when(driverRepository.save(any(Driver.class))).thenReturn(createValidDriverResponseWithIsReadyTrue());
        Driver isReadyDriver = driverService.markDriverReady(driver.getId());
        Assert.assertEquals(isReadyDriver.getIsReady().charValue(), 'Y');
    }


    @Test
    public void markDriverAsReadyWithIncompleteVerifications(){
        Driver driver = createValidDriverResponse();
        Mockito.when(driverRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(driver));
        Mockito.when(driverRepository.save(any(Driver.class))).thenReturn(createValidDriverResponseWithIsReadyTrue());
        Assert.assertThrows(VerificationPendingException.class, ()->driverService.markDriverReady(driver.getId()));
    }




    public Driver createValidDriverInput(){
        Driver driver = new Driver();
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setPassword("hash123");
        driver.setEmail("driver@abc.com");
        driver.setPhone("9123456780");

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("Car");
        vehicle.setVehicleNumber("KA-06HM-123");
        driver.setVehicle(vehicle);
        return driver;
    }


    public Driver createValidDriverResponse(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setPassword("hash123");
        driver.setEmail("driver@abc.com");
        driver.setPhone("9123456780");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        driver.setCreatedAt(timestamp);
        driver.setUpdatedAt(timestamp);
        driver.setIsActive('Y');

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("Car");
        vehicle.setVehicleNumber("KA-06HM-123");
        vehicle.setCreatedAt(timestamp);
        vehicle.setUpdatedAt(timestamp);
        driver.setVehicle(vehicle);

        Set<Verification> verificationSet = new HashSet<>();
        for(VerificationType type : VerificationType.values()){
            Verification v =  new Verification();
            v.setVerificationType(String.valueOf(type));
            v.setVerificationStatus(String.valueOf(VerificationStatus.NOT_SUMBITTED));
            v.setCreatedAt(timestamp);
            v.setUpdatedAt(timestamp);
            verificationSet.add(v);
        }
        driver.setVerifications(verificationSet);

        return driver;
    }


    public Driver createUpdatedDriverPayload(){
        Driver driver = new Driver();
        driver.setFirstName("Bill");
        driver.setLastName("Clinton");
        driver.setPassword("hash123");
        driver.setEmail("driver@abc.com");
        driver.setPhone("9123456780");
        driver.setIsActive('Y');

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("Car");
        vehicle.setVehicleNumber("KA-06HM-1234");
        driver.setVehicle(vehicle);
        return driver;
    }
    public Driver createUpdatedDriverResponse(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setFirstName("Bill");
        driver.setLastName("Clinton");
        driver.setPassword("hash123");
        driver.setEmail("driver@abc.com");
        driver.setPhone("9123456780");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        driver.setCreatedAt(timestamp);
        driver.setUpdatedAt(timestamp);
        driver.setIsActive('Y');

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("Car");
        vehicle.setVehicleNumber("KA-06HM-1234");
        vehicle.setCreatedAt(timestamp);
        vehicle.setUpdatedAt(timestamp);
        driver.setVehicle(vehicle);
        return driver;
    }


    public Driver createDeletedDriverResponse(){
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setPassword("hash123");
        driver.setEmail("driver@abc.com");
        driver.setPhone("9123456780");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        driver.setCreatedAt(timestamp);
        driver.setUpdatedAt(timestamp);
        driver.setIsActive('N');

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType("Car");
        vehicle.setVehicleNumber("KA-06HM-123");
        vehicle.setCreatedAt(timestamp);
        vehicle.setUpdatedAt(timestamp);
        driver.setVehicle(vehicle);

        Set<Verification> verificationSet = new HashSet<>();
        for(VerificationType type : VerificationType.values()){
            Verification v =  new Verification();
            v.setVerificationType(String.valueOf(type));
            v.setVerificationStatus(String.valueOf(VerificationStatus.NOT_SUMBITTED));
            v.setIsActive('N');
            v.setCreatedAt(timestamp);
            v.setUpdatedAt(timestamp);
            verificationSet.add(v);
        }
        driver.setVerifications(verificationSet);

        return driver;
    }

    public Driver createValidDriverResponseWithAllVerificationsCompleted(){
        Driver driver = createValidDriverResponse();
        for(Verification v : driver.getVerifications()){
            v.setVerificationStatus(VerificationStatus.COMPLETE.getValue());
        }
        return driver;
    }

    public Driver createValidDriverResponseWithIsReadyTrue(){
        Driver driver = createValidDriverResponseWithAllVerificationsCompleted();
        driver.setIsReady(ReadyStatus.READY.getValue());
        return driver;
    }

}
