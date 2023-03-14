package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum ActiveStatus {

    ACTIVE('Y'),
    IN_ACTIVE('N');

    Character value;

    ActiveStatus(Character c){
        this.value = c;
    }
}
