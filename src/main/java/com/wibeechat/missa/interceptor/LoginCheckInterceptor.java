package com.wibeechat.missa.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.config.RedisSessionListener;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final RedisSessionListener redisSessionListener;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // @LoginRequired 어노테이션이 없으면 인증 체크를 하지 않음
        if (!handlerMethod.hasMethodAnnotation(LoginRequired.class)) {
            return true;
        }

        // 세션이 있는지 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.info("세션이 존재하지 않습니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            log.info("세션에 userId가 없습니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return false;
        }

        // Redis에서 세션 유효성 검증
        if (!redisSessionListener.isValidSession(userId)) {
            log.info("Redis에 유효한 세션이 없습니다. userId: {}", userId);
            session.invalidate();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "세션이 만료되었습니다.");
            return false;
        }

        log.info("인증된 사용자 요청 - userId: {}", userId);
        return true;
    }
}