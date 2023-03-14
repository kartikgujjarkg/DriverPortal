package com.example.demo.controller;

import com.example.demo.entity.Verification;
import com.example.demo.service.VerificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/uber")
public class VerificationController {

    @Autowired
    VerificationServiceImpl verificationService;

    @GetMapping("/driver/{driverId}/verification")
    public ResponseEntity<Set<Verification>> getVerificationStatusOfDriver(@PathVariable("driverId") Long driverId) {
        Set<Verification> verifications =  verificationService.getAllVerificationOfDriver(driverId);
        return new ResponseEntity<>(verifications, HttpStatus.OK);
    }

    @GetMapping("/verification/{verificationId}")
    public ResponseEntity<Verification> getVerificationStatus(@PathVariable("verificationId") Long verificationId) {
        Verification verification =  verificationService.getVerification(verificationId);
        return new ResponseEntity<>(verification, HttpStatus.OK);
    }


    @PostMapping("/driver/{driverId}/verification/{verificationId}")
    public ResponseEntity<Verification> submitVerification(@PathVariable("driverId") Long driverId, @PathVariable("verificationId") Long verificationId,
    @RequestParam("file") MultipartFile file) throws IOException {
        Verification currentVerification = verificationService.submitVerification(driverId,verificationId,file);
       return new ResponseEntity<>(currentVerification, HttpStatus.ACCEPTED);
    }

}
