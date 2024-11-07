package com.wibeechat.missa.controller.user;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.component.SessionManager;
import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.entity.UserInfo;
import com.wibeechat.missa.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final SessionManager sessionManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = userService.login(request);

        if (response.isSuccess()) {
            session.setAttribute("userId", response.getUserNo());
            sessionManager.addSession(session);
        }
        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok("현재 로그인된 사용자: " + userId);
    }

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