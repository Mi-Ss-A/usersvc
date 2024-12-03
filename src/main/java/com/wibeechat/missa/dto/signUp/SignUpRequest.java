package com.wibeechat.missa.dto.signUp;

import java.time.LocalDate;

import com.wibeechat.missa.entity.mysql.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String userPassword;
    @NotBlank
    private String userName;
    @Email
    private String userEmail;
    @NotBlank
    private String userPhoneNumber;
    @NotNull
    private LocalDate userDateOfBirth;
    @NotNull
    private Gender userGender;
    @NotBlank
    private String userAddress;
}