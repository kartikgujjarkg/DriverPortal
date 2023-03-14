package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum VerificationType {

    PANCARD("PANCARD"),
    SELF_PHOTO("SELF_PHOTO"),
    DRIVING_LICENSE("DRIVING_LICENSE"),
    REGISTRATION_CERTIFICATE("REGISTRATION_CERTIFICATE"),
    BACKGROUND("BACKGROUND");

    String value;

    VerificationType(String s) {
        this.value = s;
    }
}
