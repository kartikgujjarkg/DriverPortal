package com.example.demo.service;

import com.example.demo.entity.Driver;
import com.example.demo.entity.Verification;
import com.example.demo.enums.VerificationStatus;
import com.example.demo.exception.DriverNotFoundException;
import com.example.demo.exception.VerificationNotFoundException;
import com.example.demo.kafka.KafkaTaskProducerService;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.VerificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;


@Service
public class VerificationServiceImpl implements VerificationService {


    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    KafkaTaskProducerService kafkaTaskProducerService;

    private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    public Set<Verification> getAllVerificationOfDriver(Long driverId){
        Set<Verification>  verficationSet = verificationRepository.findByDriverId(driverId);
        return verficationSet;
    }

    public Verification getVerification(Long verificationId) {
        Optional<Verification> verificationOptional = verificationRepository.findById(verificationId);
        if(!verificationOptional.isPresent()){
            throw new VerificationNotFoundException();
        }
        return verificationOptional.get();
    }

    public Verification submitVerification(Long driverId, Long verificationId, MultipartFile file) throws IOException {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(!driver.isPresent()){
           throw new DriverNotFoundException();
        }
        Optional<Verification> verificationOptional = verificationRepository.findById(verificationId);
        if(!verificationOptional.isPresent()){
            throw new VerificationNotFoundException();
        }
        Verification verification = verificationOptional.get();
        verification.setVerificationStatus(VerificationStatus.SUMBITTED.getValue());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        verification.setUpdatedAt(timestamp);
        verificationRepository.save(verification);
        //Send Message to Task Service to create verification task
        kafkaTaskProducerService.sendMessageToCreateVerificationTask(verification);
        return verification;
    }

    public void updateVerificationStatus(Verification verification){
        Long verificationId = verification.getId();
        Optional<Verification> currentVerificationOptional = verificationRepository.findById(verificationId);
        Verification currentVerification = currentVerificationOptional.get();
        currentVerification.setVerificationStatus(verification.getVerificationStatus());
        currentVerification.setComment(verification.getComment());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        currentVerification.setUpdatedAt(timestamp);
        verificationRepository.save(currentVerification);
        checkAndNotifyUserIfNeeded(currentVerification);
    }

    public void checkAndNotifyUserIfNeeded(Verification verification) {
        if(verification.getVerificationStatus().equals(VerificationStatus.DECLINED.getValue())){
            //Notify driver with decline details
            logger.info("Your verification has been declined for: " + verification.getVerificationType());
            logger.info(verification.getComment());
        }
        Set<Verification> verifications = verification.getDriver().getVerifications();
        boolean pendingVerificationFound = false;
        StringJoiner excpMsgJoiner = new StringJoiner(",");
        for (Verification v : verifications) {
            if (!verification.getVerificationStatus().equals(VerificationStatus.COMPLETE.getValue())) {
                excpMsgJoiner.add(verification.getVerificationType());
                pendingVerificationFound = true;
            }
        }
        if(!pendingVerificationFound){
            //Notify driver that his verification is complete
            logger.info("Hurray! Your verification has been completed !!");
        }
    }



}
