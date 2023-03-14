package com.example.demo.entity;

import com.google.protobuf.Timestamp;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "driver_verification_activity_log")
public class DriverVerificationActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "verification_id")
    private Long verificationId;

    @Column(name = "prev_state")
    private String prevState;

    @Column(name = "current_state")
    private String currentState;

    @Column(name="created_at",nullable = false)
    private Timestamp createdAt;
}
