package com.example.BC_alternance.filters;

import com.example.BC_alternance.service.impl.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> PUBLIC_PATTERNS = List.of(
            "/utilisateurs/login",
            "/utilisateurs/register",
            "/utilisateurs/validate-email",
            "/utilisateurs/resend-code",
            "/utilisateurs/check-email",
            "/utilisateurs/firebase-login",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**",
            "/bornes",           // seulement GET /bornes
            "/bornes/*",
            "/bornes/upload/*"    // seulement GET /bornes/{id}, si tu les autorises
            // PAS de /bornes/** ici, sinon ça inclut aussi /bornes/user/bornes
    );


    private static final Logger logger = LoggerFactory.getLogger(JWTTokenFilter.class);

    public JWTTokenFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        logger.info("JWTTokenFilter - Processing request to: " + requestURI);

        boolean isPublicUrl = PUBLIC_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));

        if (isPublicUrl) {
            logger.info("Skipping JWT validation for public URI: " + requestURI);
            filterChain.doFilter(request, response); // Let the request proceed immediately
            return; // **Crucial: Exit the filter method**
        }

        // If it's not a public URL, then proceed with token validation
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            logger.warn("No Bearer token found or invalid format for protected URI: " + requestURI);
            // Don't call filterChain.doFilter() here for protected routes with missing/invalid token
            // Spring Security's subsequent filters (like exception handling for authentication) will manage
            // the 401/403 for protected endpoints.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return; // Terminate processing for unauthorized access
        }

        String extractedToken = header.substring(7).trim();
        logger.info("Extracted token for protected URI: " + requestURI);

        if (!tokenService.validateToken(extractedToken)) {
            logger.error("Invalid JWT token for protected URI: " + requestURI);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return; // Terminate processing for invalid token
        }

        try {
            String email = tokenService.extractUsernameFromToken(extractedToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("SecurityContext updated for user: " + email + " for URI: " + requestURI);
        } catch (Exception e) {
            logger.error("Error setting authentication for token on URI: " + requestURI + " : " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
            return; // Terminate processing on authentication setup failure
        }

        filterChain.doFilter(request, response); // Continue the filter chain for authenticated requests
    }
}