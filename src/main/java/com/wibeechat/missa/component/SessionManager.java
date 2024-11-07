package com.wibeechat.missa.component;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class SessionManager {
    private final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public void addSession(HttpSession session) {
        sessions.put(session.getId(), session);
    }

    public void removeSession(String sessionId) {
        HttpSession session = sessions.remove(sessionId);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // 이미 무효화된 세션
                log.warn("Session already invalidated: {}", sessionId);
            }
        }
    }

    public void clearExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.forEach((id, session) -> {
            try {
                long lastAccessedTime = session.getLastAccessedTime();
                if (now - lastAccessedTime > session.getMaxInactiveInterval() * 1000L) {
                    removeSession(id);
                }
            } catch (IllegalStateException e) {
                // 이미 무효화된 세션 제거
                sessions.remove(id);
            }
        });
    }
}