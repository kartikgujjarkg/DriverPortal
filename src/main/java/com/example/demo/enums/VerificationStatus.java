package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum VerificationStatus {
    NOT_SUMBITTED("NOT_SUMBITTED"),
    SUMBITTED("SUMBITTED"),
    INPROGRESS("INPROGRESS"),
    DECLINED("DECLINED"),
    COMPLETE("COMPLETE");

    String value;

    VerificationStatus(String value){
        this.value = value;
    }

    }
