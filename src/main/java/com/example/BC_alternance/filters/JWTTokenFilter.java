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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UserDetailsService userDetailsService;
    public JWTTokenFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }


    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    // chaine de filtre qui vont s'excuter avant le controller
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.info("JWTTokenFilter");

        //lire le hearder pour extraire le token
        // un hearders de requete est un ensemble de header (sur postman on voit tous les headers
       String header =  request.getHeader(HttpHeaders.AUTHORIZATION.toLowerCase());


        if (header == null || !header.startsWith("Bearer ") || header.isEmpty()) {
            // si ce n'est pas null, pas vide ou qui ne commence pas par "bearer "
            //alors tu passee à la requete suivante
            filterChain.doFilter(request, response);
            return;
        }

        //
        String extractedToken = header.split(" ")[1].trim();
        logger.info("Extracted token: " + extractedToken);

        //si token valide alors passe à la suite sinon passe pas
        if (!tokenService.validateToken(extractedToken)) {
            filterChain.doFilter(request, response);
            return;
        }
        // si on arrive ici, c'est que ke token est valide
        // donc je veux authentifier l'utilisateur dans springSecurity
        // (donc créer un objet authentication, et met le dans le contexte de sécurité)
        // donc il faut extraire du token le username

        String email = tokenService.extractUsernameFromToken(extractedToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("SecurityContext updated");
        filterChain.doFilter(request, response);
    }


}
