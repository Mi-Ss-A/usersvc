package com.wibeechat.missa.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisSessionListener implements HttpSessionListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_SESSION_PREFIX = "user:session:";
    private static final String SESSION_USER_PREFIX = "session:user:";

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        String userId = (String) se.getSession().getAttribute("userId");
        log.info("세션 생성 - SessionId: {}, UserId: {}", sessionId, userId);

        if (userId != null) {
            // userId로 sessionId 찾을 수 있도록 저장
            redisTemplate.opsForValue().set(USER_SESSION_PREFIX + userId, sessionId);
            // sessionId로 userId 찾을 수 있도록 저장
            redisTemplate.opsForValue().set(SESSION_USER_PREFIX + sessionId, userId);
            log.info("Redis에 세션-사용자 매핑 저장 완료");
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        log.info("세션 종료: {}", sessionId);

        try {
            // sessionId로 userId 조회
            String userId = (String) redisTemplate.opsForValue().get(SESSION_USER_PREFIX + sessionId);

            if (userId != null) {
                // 세션 관련 데이터 모두 삭제
                redisTemplate.delete(USER_SESSION_PREFIX + userId);
                redisTemplate.delete(SESSION_USER_PREFIX + sessionId);
                redisTemplate.delete("spring:session:sessions:" + sessionId);
                redisTemplate.delete("spring:session:sessions:expires:" + sessionId);

                log.info("Redis 세션 데이터 삭제 완료 - SessionId: {}, UserId: {}", sessionId, userId);
            }
        } catch (Exception e) {
            log.error("Redis 세션 삭제 중 오류 발생: {}", sessionId, e);
        }
    }

    // 세션 유효성 확인 메서드 추가
    public boolean isValidSession(String userId) {
        String sessionId = (String) redisTemplate.opsForValue().get(USER_SESSION_PREFIX + userId);
        return sessionId != null &&
                redisTemplate.hasKey("spring:session:sessions:" + sessionId);
    }
}