package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum ReadyStatus {

    READY('Y'),
    NOT_READY('N');

    Character value;

    ReadyStatus(Character c){
        this.value = c;
    }
}
