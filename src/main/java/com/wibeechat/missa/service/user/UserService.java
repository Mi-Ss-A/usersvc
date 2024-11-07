package com.wibeechat.missa.service.user;


import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.entity.UserInfo;
import com.wibeechat.missa.exception.ErrorCode;
import com.wibeechat.missa.exception.ErrorResponse;
import com.wibeechat.missa.exception.InvalidPasswordException;
import com.wibeechat.missa.exception.UserNotFoundException;
import com.wibeechat.missa.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;

// UserService.java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        UserInfo user = userInfoRepository.findByUserId(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        // 비밀번호 검증 (실제로는 암호화된 비밀번호를 비교해야 함)
        if (!user.getUserPw().equals(request.getUserPassword())) {
            throw new InvalidPasswordException();
        }

        return LoginResponse.success(user.getUserNo());
    }

    public UserInfo getUserInfo(String userNo) {
        UserInfo user = userInfoRepository.findById(userNo)
                .orElseThrow(UserNotFoundException::new);

        return UserInfo.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .build();
    }
}