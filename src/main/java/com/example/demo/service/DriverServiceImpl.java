package com.example.demo.service;

import com.example.demo.entity.Driver;
import com.example.demo.entity.Vehicle;
import com.example.demo.entity.Verification;
import com.example.demo.enums.ActiveStatus;
import com.example.demo.enums.ReadyStatus;
import com.example.demo.enums.VerificationStatus;
import com.example.demo.enums.VerificationType;
import com.example.demo.exception.DriverAlreadyPresentException;
import com.example.demo.exception.DriverNotFoundException;
import com.example.demo.exception.VehicleDetailsMissingException;
import com.example.demo.exception.VerificationPendingException;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    public Driver registerDriver(Driver driver) {
        //driver.setPassword(EncodeUtils.encode(driver.getPassword()));
        checkIfPhoneNumberIsTaken(driver);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        driver.setCreatedAt(timestamp);
        driver.setUpdatedAt(timestamp);
        addVehicleToDriver(driver);
        driver.setIsActive(ActiveStatus.ACTIVE.getValue());
        createInitialVerificationSteps(driver);
        Driver createdDriver = driverRepository.save(driver);
        return createdDriver;
    }


    public Driver getDriver(Long driverId) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (!driverOptional.isPresent()) {
            throw new DriverNotFoundException();
        }
        Driver driver = driverOptional.get();
        return driver;
    }

    public Driver updateDriver(Driver driver, Long driverId) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (!driverOptional.isPresent()) {
            throw new DriverNotFoundException();
        }
        Driver existingDriver = driverOptional.get();
        validatePhoneNumberIfChanged(existingDriver, driver);
        validateIfVehicleDetailsPresent(driver);
        validateIfIsReadyIsChanged(existingDriver, driver);
        existingDriver.setVehicle(driver.getVehicle());
        existingDriver.setPhone(driver.getPhone());
        existingDriver.setFirstName(driver.getFirstName());
        existingDriver.setLastName(driver.getLastName());
        existingDriver.setEmail(driver.getEmail());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        existingDriver.setUpdatedAt(timestamp);
        return driverRepository.save(existingDriver);
    }


    public Driver deleteDriver(Long driverId) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (!driverOptional.isPresent()) {
            throw new DriverNotFoundException();
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //Soft delete driver
        Driver driver = driverOptional.get();
        driver.setIsActive(ActiveStatus.IN_ACTIVE.getValue());
        driver.setUpdatedAt(timestamp);
        //Soft delete verifications associated
        driver.getVerifications().stream().forEach(v -> {
            v.setIsActive(ActiveStatus.IN_ACTIVE.getValue());
            v.setUpdatedAt(timestamp);
        });
        //Soft delete vehicle associated
        driver.getVehicle().setIsActive(ActiveStatus.IN_ACTIVE.getValue());
        driver.getVehicle().setUpdatedAt(timestamp);
        return driverRepository.save(driver);
    }

    @Override
    public Driver markDriverReady(Long driverId) {

        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (!driverOptional.isPresent()) {
            throw new DriverNotFoundException();
        }
        Driver driver = driverOptional.get();
        //Validate if there are no pending verifications
        checkIfAnyPendingVerification(driver);

        driver.setIsReady(ReadyStatus.READY.getValue());
        driverRepository.save(driver);
        return driver;
    }

    public Driver addVehicleDetailsForDriver(Long driverId, Vehicle vehicle) {
        Optional<Driver> driverOptional = driverRepository.findById(driverId);
        if (!driverOptional.isPresent()) {
            throw new DriverNotFoundException();
        }
        Driver driver = driverOptional.get();
        return driver;
    }


    public void createInitialVerificationSteps(Driver driver) {

        Set<Verification> verificationSet = new HashSet<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (VerificationType type : VerificationType.values()) {
            Verification v = new Verification();
            v.setVerificationType(String.valueOf(type));
            v.setVerificationStatus(String.valueOf(VerificationStatus.NOT_SUMBITTED));
            v.setCreatedAt(timestamp);
            v.setUpdatedAt(timestamp);
            verificationSet.add(v);
        }
        driver.setVerifications(verificationSet);
    }

    public void checkIfPhoneNumberIsTaken(Driver driver) {
        Optional<Driver> Driver = Optional.ofNullable(driverRepository.findByPhone(driver.getPhone()));
        if (Driver.isPresent()) {
            throw new DriverAlreadyPresentException("This phone is already registered");
        }
    }

    public void validatePhoneNumberIfChanged(Driver existingDriver, Driver updatedDriver) {
        if (!existingDriver.getPhone().equals(updatedDriver.getPhone())) {
            checkIfPhoneNumberIsTaken(updatedDriver);
        }
    }

    public void validateIfVehicleDetailsPresent(Driver driver) {
        if (driver.getVehicle() == null) {
            throw new VehicleDetailsMissingException();
        }
    }

    public void validateIfIsReadyIsChanged(Driver existingDriver, Driver driver) {
        if (!existingDriver.getIsReady().equals(driver.getIsReady()) &&
                driver.getIsReady().equals(ReadyStatus.READY.getValue())) {
            checkIfAnyPendingVerification(driver);
        }
    }

    public void addVehicleToDriver(Driver driver) {
        if (driver.getVehicle() != null && driver.getVehicle().getVehicleNumber() != null
                && driver.getVehicle().getVehicleType() != null) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            driver.getVehicle().setCreatedAt(timestamp);
            driver.getVehicle().setUpdatedAt(timestamp);
            driver.getVehicle().setIsActive(ActiveStatus.ACTIVE.getValue());
        }
    }

    public void checkIfAnyPendingVerification(Driver driver) {
        Set<Verification> verifications = driver.getVerifications();
        boolean pendingVerificationFound = false;
        StringJoiner excpMsgJoiner = new StringJoiner(",");
        for (Verification verification : verifications) {
            if (!verification.getVerificationStatus().equals(VerificationStatus.COMPLETE.getValue())) {
                excpMsgJoiner.add(verification.getVerificationType());
                pendingVerificationFound = true;
            }
            if(pendingVerificationFound) {
                String excpMsg = excpMsgJoiner.toString() + " verification needs to be completed";
                throw new VerificationPendingException(excpMsg);
            }
        }
    }
}
