package com.example.demo.enums;

public enum VehicleType {
    BIKE("BIKE"),
    AUTO("AUTO"),
    CAR("CAR");

    String value;

    VehicleType(String s) {
        this.value = s;
    }
}
