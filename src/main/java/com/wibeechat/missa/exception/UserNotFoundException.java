package com.wibeechat.missa.exception;

// UserNotFoundException.java
public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
