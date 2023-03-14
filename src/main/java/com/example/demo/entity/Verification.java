package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "verification_type", nullable = false, length = 40)
    private String verificationType;

    @Column(name = "verification_status", nullable = false, length = 20)
    private String verificationStatus;

    @Column(name = "verification_file_url", length = 200)
    private String verificationFileUrl;

    @Column(name = "comment",length =400)
    private String comment;

    @Column(name = "created_at",nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    private Timestamp updatedAt;

    @Column(name = "is_active",nullable = false)
    private Character isActive = 'Y';

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Driver driver;

}
