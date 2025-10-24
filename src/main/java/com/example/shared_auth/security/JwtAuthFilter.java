package com.example.shared_auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        try {
            // 1️⃣ Extract Authorization header
            String header = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // 2️⃣ Check if header starts with "Bearer "
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7); // remove "Bearer "
                username = jwtUtils.getUsernameFromToken(token);
            }

            // 3️⃣ If we got username and no one is authenticated yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4️⃣ Validate the token
                if (jwtUtils.validateJwtToken(token)) {
                    // 5️⃣ Build authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    // DEBUG: print authorities so we can confirm which roles were assigned
                    System.out.println("[JwtAuthFilter] Authorities for " + username + ": " + userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 6️⃣ Store authentication in context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception e) {
            System.err.println("JWT authentication failed: " + e.getMessage());
        }

        // 7️⃣ Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
