package com.WorkSphere.WorkSphereBackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.
    UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.
    SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.
    WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import java.io.IOException;
import java.util.Collections;
 
@Component
public class JWTFilter extends OncePerRequestFilter {
 
    @Autowired
    private JWTUtil jwtUtil;
 
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
 
        final String authHeader = request.getHeader("Authorization");
 
        String token = null;
        String email = null;
        String role = null;
 
        // 1️⃣ Check if header exists and starts with Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
 
            token = authHeader.substring(7); // remove "Bearer "
 
            // 2️⃣ Validate token
            if (jwtUtil.validateToken(token)) {
 
                email = jwtUtil.extractEmail(token);
                role = jwtUtil.extractRole(token);
            }
        }
 
        // 3️⃣ If valid and not already authenticated
        if (email != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {
 
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role);
 
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(authority)
                    );
 
            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );
 
            // 4️⃣ Set authentication in security context
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }
 
        filterChain.doFilter(request, response);
    }
}