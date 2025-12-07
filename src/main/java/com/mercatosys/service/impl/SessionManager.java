package com.mercatosys.service.impl;

import com.mercatosys.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;




@Component
public class SessionManager {

    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    public User getUserBySessionId(String sessionId) {
        if (sessionId == null) return null;
        return sessions.get(sessionId);
    }

    public Long getUserIdBySessionId(String sessionId) {
        User user = getUserBySessionId(sessionId);
        return (user != null) ? user.getId() : null;
    }

    public void invalidateSession(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }

    public boolean isValidSession(String sessionId) {
        return sessionId != null && sessions.containsKey(sessionId);
    }
}

