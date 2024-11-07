package com.wibeechat.missa.entity.mysql;


import lombok.Getter;

@Getter
public enum UserStatus {
    A("ACTIVE"),
    I("INACTIVE"),
    D("DELETED");

    private final String code;

    UserStatus(String code) {
        this.code = code;
    }
}