package com.wibeechat.missa.controller.user;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.component.SessionManager;
import com.wibeechat.missa.config.RedisSessionListener;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final RedisSessionListener redisSessionListener;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = userService.login(request);

        if (response.isSuccess()) {
            String userId = response.getUserNo();
            session.setAttribute("userId", userId);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            session.invalidate();
            response.put("success", true);
            response.put("message", "로그아웃 되었습니다.");
            return ResponseEntity.ok(response);
        }
        response.put("success", false);
        response.put("message", "세션이 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(@CurrentUser String userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("isValid", redisSessionListener.isValidSession(userId));
        return ResponseEntity.ok(response);
    }
}