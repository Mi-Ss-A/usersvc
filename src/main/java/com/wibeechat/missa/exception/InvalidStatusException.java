package com.wibeechat.missa.exception;

public class InvalidStatusException extends BaseException {
    public InvalidStatusException() {
        super(ErrorCode.INVALID_STATUS);
    }
}
