package com.example.BC_alternance.config;

import com.example.BC_alternance.filters.JWTTokenFilter;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
				.cors(Customizer.withDefaults())
				.headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(request -> request
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
						.requestMatchers("/utilisateurs/login","/utilisateurs/register" , "/actuator/**", "/utilisateurs/firebase-login").permitAll()
						.requestMatchers("/utilisateurs/validate-email","/utilisateurs/check-email","/utilisateurs/resend-code").permitAll()
					//	.requestMatchers(HttpMethod.POST, "/utilisateurs/register").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/geocode").permitAll()
						.requestMatchers(HttpMethod.GET, "/bornes/**").permitAll()
						.requestMatchers(HttpMethod.DELETE, "/bornes/**").authenticated()
						.requestMatchers(HttpMethod.POST, "/bornes/search").permitAll()
						.requestMatchers(HttpMethod.POST, "/bornes/user/bornes").authenticated()
						.requestMatchers(HttpMethod.PUT, "/bornes/user/bornes/**").authenticated()
						.requestMatchers(HttpMethod.GET, "/lieux/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/lieux").authenticated()
						.requestMatchers(HttpMethod.POST, "/reservations/check-availability").permitAll()
						.requestMatchers(HttpMethod.POST, "/reservations").authenticated()
						.anyRequest().authenticated()

				)
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
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


