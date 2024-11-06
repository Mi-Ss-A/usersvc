package com.wibeechat.missa.exception;

// InvalidPasswordException.java
public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}