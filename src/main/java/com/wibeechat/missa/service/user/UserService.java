package com.wibeechat.missa.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.entity.mysql.UserInfo;
import com.wibeechat.missa.exception.InvalidPasswordException;
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

        if (!user.getUserPw().equals(request.getUserPassword())) {
            throw new InvalidPasswordException();
        }

        return LoginResponse.success(user.getUserNo());
    }

    public UserInfo getUserInfo(String userNo) {
        return userInfoRepository.findById(userNo)
                .orElseThrow(UserNotFoundException::new);
    }
}
