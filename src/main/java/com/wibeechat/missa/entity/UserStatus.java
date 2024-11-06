package com.wibeechat.missa.entity;


import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("A"),
    INACTIVE("I"),
    DELETED("D");

    private final String code;

    UserStatus(String code) {
        this.code = code;
    }
}