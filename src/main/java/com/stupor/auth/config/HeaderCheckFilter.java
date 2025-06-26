package com.stupor.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//@Component
public class HeaderCheckFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String gatewayHeader = request.getHeader("gateway");
        if (gatewayHeader == null || !gatewayHeader.equals("186Kdc9899")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Запрос должен пройти через gateway");
            return;
        }
        filterChain.doFilter(request, response);
    }
}