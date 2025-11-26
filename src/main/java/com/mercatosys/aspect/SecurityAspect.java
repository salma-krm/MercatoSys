package com.mercatosys.aspect;
import com.mercatosys.annotation.RequireRole;
import com.mercatosys.entity.User;
import com.mercatosys.enums.Role;
import com.mercatosys.service.impl.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityAspect {

    private final SessionManager sessionManager;
    private final HttpServletRequest request;


    @Before("@annotation(com.mercatosys.annotation.RequireAuth)")
    public void checkAuthentication(JoinPoint joinPoint) {
        String sessionId = request.getHeader("Session-Id");
        if (sessionId == null || !sessionManager.isValidSession(sessionId)) {
            throw new SecurityException("Accès refusé : utilisateur non authentifié !");
        }
        log.info(" Authentification réussie pour la méthode {}", joinPoint.getSignature().getName());
    }
    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        String sessionId = request.getHeader("Session-Id");

        if (sessionId == null || !sessionManager.isValidSession(sessionId)) {
            throw new SecurityException(" Accès refusé : utilisateur non authentifié !");
        }

        User user = sessionManager.getUserBySessionId(sessionId);
        Role requiredRole = Role.valueOf(requireRole.value().toUpperCase());

        if (user.getRole() != requiredRole) {
            throw new SecurityException(" Accès refusé : rôle " + requiredRole + " requis !");
        }

        log.info(" Accès autorisé pour {} avec le rôle {}", user.getEmail(), user.getRole());
    }
}
