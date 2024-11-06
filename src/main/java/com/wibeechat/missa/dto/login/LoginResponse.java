package com.wibeechat.missa.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// LoginResponse.java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean isSuccess;
    private int code;
    private String userNo;

    public static LoginResponse of(boolean isSuccess, int code, String userNo) {
        return LoginResponse.builder()
                .isSuccess(isSuccess)
                .code(code)
                .userNo(userNo)
                .build();
    }

    public static LoginResponse success(String userNo) {
        return of(true, 200, userNo);
    }

    public static LoginResponse fail() {
        return of(false, 204, null);
    }
}