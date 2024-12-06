package com.wibeechat.missa.service.user;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.dto.signUp.SignUpRequest;
import com.wibeechat.missa.entity.mysql.UserInfo;
import com.wibeechat.missa.exception.InvalidPasswordException;
import com.wibeechat.missa.exception.InvalidStatusException;
import com.wibeechat.missa.exception.UserNotFoundException;
import com.wibeechat.missa.repository.mysql.UserInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        UserInfo user = userInfoRepository.findByUserId(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if ("I".equals(user.getUserStatus()) || "D".equals(user.getUserStatus())) {
            throw new InvalidStatusException();
        }

        if (!user.getUserPw().equals(request.getUserPassword())) {
            throw new InvalidPasswordException();
        }

        return LoginResponse.success(user.getUserNo());
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(String userNo) {
        return userInfoRepository.findById(userNo)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void signUp(SignUpRequest request) {
        if (userInfoRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 ID입니다.");
        }

        UserInfo newUser = UserInfo.builder()
                .userNo(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .userPw(request.getUserPassword()) // 비밀번호는 해시 처리 권장
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userPhoneNumber(request.getUserPhoneNumber())
                .userDateOfBirth(request.getUserDateOfBirth())
                .userGender(request.getUserGender())
                .userAddress(request.getUserAddress())
                .userType(request.getUserType())
                .build();

        userInfoRepository.save(newUser);
    }
}
