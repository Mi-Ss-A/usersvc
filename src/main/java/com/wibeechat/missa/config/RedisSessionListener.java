package com.wibeechat.missa.config;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisSessionListener{
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_SESSION_PREFIX = "user:session:";
    private static final String SESSION_USER_PREFIX = "session:user:";
    private static final int SESSION_TIMEOUT = 30 * 60; // 30분

    public void sessionCreated(String sessionId, String userId) {
        if (userId != null) {
            // userId로 sessionId 찾을 수 있도록 저장
            redisTemplate.opsForValue().set(USER_SESSION_PREFIX + userId, sessionId, SESSION_TIMEOUT, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(SESSION_USER_PREFIX + sessionId, userId, SESSION_TIMEOUT, TimeUnit.SECONDS);
            log.info("Redis에 세션-사용자 매핑 저장 완료");
        }
    }

    public void sessionDestroyed(String sessionId) {
        log.info("세션 종료: {}", sessionId);

        try {
            // sessionId로 userId 조회
            String userId = (String) redisTemplate.opsForValue().get(SESSION_USER_PREFIX + sessionId);

            if (userId != null) {
                // 세션 관련 데이터 모두 삭제
                deleteSessionData(userId, sessionId);
                log.info("Redis 세션 데이터 삭제 완료 - SessionId: {}, UserId: {}", sessionId, userId);
            }
        } catch (Exception e) {
            log.error("Redis 세션 삭제 중 오류 발생: {}", sessionId, e);
        }
    }

    private void deleteSessionData(String userId, String sessionId) {
        redisTemplate.delete(USER_SESSION_PREFIX + userId);
        redisTemplate.delete(SESSION_USER_PREFIX + sessionId);
    }

    public boolean isValidSession(String userId) {
        String sessionId = (String) redisTemplate.opsForValue().get(USER_SESSION_PREFIX + userId);
        if (sessionId == null) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(SESSION_USER_PREFIX + sessionId));
    }
}