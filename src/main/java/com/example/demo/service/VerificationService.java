package com.example.demo.service;

import com.example.demo.entity.Verification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface VerificationService {
    public Set<Verification> getAllVerificationOfDriver(Long driverId);

    public Verification getVerification(Long verificationId);

    public Verification submitVerification(Long driverId, Long verificationId, MultipartFile file) throws IOException;

    public void updateVerificationStatus(Verification verification);
}
