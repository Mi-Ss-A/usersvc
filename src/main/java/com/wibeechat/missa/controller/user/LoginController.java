package com.wibeechat.missa.controller.user;

import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// UserController.java
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = userService.login(request);

        if(response.isSuccess()){
            session.setAttribute("userId", response.getUserNo());
        }
        return ResponseEntity.ok(response);
    }

    // 세션 체크를 위한 인터셉터나 어노테이션을 추가하면 좋습니다
    @GetMapping("/check-session")
    public ResponseEntity<String> checkSession(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다.");
        }
        return ResponseEntity.ok("현재 로그인된 사용자: " + userId);
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}