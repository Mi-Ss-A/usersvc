package com.wibeechat.missa.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

// ErrorCode.java
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST
    INVALID_INPUT_VALUE(400, "Invalid Input Value"),

    // 401 UNAUTHORIZED
    INVALID_PASSWORD(401, "Invalid Password"),

    // 404 NOT_FOUND
    USER_NOT_FOUND(404, "User Not Found"),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int status;
    private final String message;
}