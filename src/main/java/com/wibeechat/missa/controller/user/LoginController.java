package com.wibeechat.missa.controller.user;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.component.SessionManager;
import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.entity.mysql.UserInfo;
import com.wibeechat.missa.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final SessionManager sessionManager;

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "없는 사용자")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = userService.login(request);

        if (response.isSuccess()) {
            session.setAttribute("userId", response.getUserNo());
            sessionManager.addSession(session);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "세션 확인", description = "현재 로그인된 세션을 확인합니다.")
    @ApiResponse(responseCode = "200", description = "세션 확인 성공")
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    @LoginRequired
    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        return ResponseEntity.ok("현재 로그인된 사용자: " + userId);
    }


    @Operation(summary = "로그아웃", description = "현재 세션을 종료합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @LoginRequired
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        sessionManager.removeSession(session.getId());
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @LoginRequired
    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getProfile(@CurrentUser String userId) {
        UserInfo userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }
}