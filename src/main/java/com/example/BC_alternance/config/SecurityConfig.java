package com.example.BC_alternance.config;

import com.example.BC_alternance.filters.JWTTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

	@Autowired
	private JWTTokenFilter jwtTokenFilter;


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.authorizeHttpRequests(
						request -> request
								.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Autoriser Swagger
//								.requestMatchers("/actuator/**").permitAll()
//								.requestMatchers("/utilisateurs/login").permitAll()
//								.requestMatchers("/bornes").permitAll()
//								.requestMatchers("/utilisateurs").permitAll()
								.requestMatchers("/**").permitAll()
						//.requestMatchers("/private").hasAnyRole("USER", "ADMIN")
						//.requestMatchers("/user").hasRole("ADMIN")
						.anyRequest().authenticated())
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

					// param 1 le filtre, param 2 celui qui succède le filtre
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			HttpSecurity http,
			PasswordEncoder encoder,
			UserDetailsService userDetailsService
	) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(encoder)
				.and()
				.build();
	}
}
