package com.omon4412.authservice.config;

import com.omon4412.authservice.model.SessionDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class NewLoginFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/login")) {
            HttpSession session = request.getSession(true);
            if (session != null) {
                String remoteAddr = request.getRemoteAddr();
                String userAgent = request.getHeader("User-Agent");
                SessionDetails sessionDetails = new SessionDetails();
                sessionDetails.setRemoteAddress(remoteAddr);
                sessionDetails.setUserAgent(userAgent);
                session.setAttribute("SESSION_DETAILS", sessionDetails);
            }
        }
        filterChain.doFilter(request, response);
    }
}
