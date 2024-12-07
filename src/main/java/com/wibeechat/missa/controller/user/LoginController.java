package com.wibeechat.missa.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.config.RedisSessionListener;
import com.wibeechat.missa.dto.login.LoginRequest;
import com.wibeechat.missa.dto.login.LoginResponse;
import com.wibeechat.missa.dto.signUp.SignUpRequest;
import com.wibeechat.missa.entity.mysql.UserInfo;
import com.wibeechat.missa.exception.InvalidPasswordException;
import com.wibeechat.missa.exception.UserNotFoundException;
import com.wibeechat.missa.service.user.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final RedisSessionListener redisSessionListener;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            LoginResponse response = userService.login(request);

            if (response.isSuccess()) {
                String userId = response.getUserNo();
                session.setAttribute("userId", userId);
                redisSessionListener.sessionCreated(session.getId(), userId);
            }
            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "사용자를 찾을 수 없습니다."));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "비밀번호가 올바르지 않습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session, @CurrentUser String userId) {
        Map<String, Object> response = new HashMap<>();
        if (userId != null) {
            session.invalidate();
            redisSessionListener.sessionDestroyed(session.getId());
        }
        response.put("success", true);
        response.put("message", "로그아웃 되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(@CurrentUser String userId) {
        Map<String, Object> response = new HashMap<>();
        String redisSessionId = redisSessionListener.getRedisSessionId(userId);
        response.put("redisSessionId", redisSessionId);
        redisSessionListener.refreshSessionTTL(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    @LoginRequired
    public ResponseEntity<Map<String, Object>> info(@CurrentUser String userId) {
        Map<String, Object> response = new HashMap<>();
        UserInfo data = userService.getUserInfo(userId);
        String status = data.getUserStatus().toString();
        response.put("status", status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignUpRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.signUp(request);
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
