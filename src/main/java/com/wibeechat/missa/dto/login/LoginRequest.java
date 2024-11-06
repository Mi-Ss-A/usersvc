package com.wibeechat.missa.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// LoginRequest.java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String userId;
    private String userPassword;
}