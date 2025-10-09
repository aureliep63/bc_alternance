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
import java.util.List;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {
    //s'exécute à chaque req pour valider le token

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    //pas de vérif sur ces routes
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
            "/bornes",
            "/bornes/upload/*" ,
            "/bornes/search",
            "/api/geocode",
            "/reservations/check-availability"
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

        // Si url public, alors on laisse passer
        if (isPublicUrl) {
            logger.info("Skipping JWT validation for public URI: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // si header absent ou pas ok, alors 401
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            logger.warn("No Bearer token found or invalid format for protected URI: " + requestURI);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        // extraction du token et validation
        String extractedToken = header.substring(7).trim();
        logger.info("Extracted token for protected URI: " + requestURI);
        if (!tokenService.validateToken(extractedToken)) {
            logger.error("Invalid JWT token for protected URI: " + requestURI);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        // chargement du user
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
            return;
        }

        // si tout est ok => controller
        filterChain.doFilter(request, response);
    }
}