package com.wibeechat.missa;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.entity.mysql.Gender;
import com.wibeechat.missa.entity.mysql.UserInfo;
import com.wibeechat.missa.entity.mysql.UserStatus;
import com.wibeechat.missa.repository.mysql.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 통합 테스트
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @BeforeEach
    void setUp() {
        userInfoRepository.deleteAll();

        UserInfo user = new UserInfo();
        user.setUserNo(UUID.randomUUID().toString());  // UUID를 String으로 변환
        user.setUserPhoneNumber("010-1234-5678");
        user.setUserName("홍민영");
        user.setUserEmail("hong@example.com");
        user.setUserDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setUserGender(Gender.M);  // Enum 사용
        user.setUserAddress("서울시 강남구 테헤란로 123");
        user.setUserRegistrationDate(LocalDateTime.now());
        user.setUserStatus(UserStatus.A);  // Enum 사용
        user.setUserType("NORMA");  // 또는 상수로 정의된 사용자 타입
        // 또는
        // user.setUserNo(String.valueOf(UUID.randomUUID()));

        user.setUserId("hongminyeong");
        user.setUserPw("1234");
        // 다른 필수 필드들 설정
        userInfoRepository.save(user);
    }

    @Test
    @DisplayName("실제 DB를 사용한 로그인 성공 테스트")
    void loginSuccess() throws Exception {
        // given

        LoginRequest request = new LoginRequest("hongminyeong", "1234");

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))  // isSuccess -> success
                .andExpect(jsonPath("$.code").value(200))
                .andDo(print());
    }

    @Test
    @DisplayName("실제 DB를 사용한 로그인 실패 테스트 - 존재하지 않는 사용자")
    void loginFailUserNotFound() throws Exception {
        // given
        LoginRequest request = new LoginRequest("unknown", "1234");

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andDo(print());
    }
}