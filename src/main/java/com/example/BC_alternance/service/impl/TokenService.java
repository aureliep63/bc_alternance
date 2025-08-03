package com.example.BC_alternance.service.impl;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier; // Pour la validation HS256
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64; // Pour gérer la clé secrète en Base64
import org.slf4j.Logger; // Importez Logger
import org.slf4j.LoggerFactory; // Importez LoggerFactory



@Service
public class TokenService {
	@Value("${jwt.secret-key}")
	private String secretKey ;

	private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

	private SecretKeySpec getSecretKeySpec() {
		if (secretKey == null || secretKey.isEmpty()) {
			throw new IllegalArgumentException("JWT secret key must not be null or empty.");
		}
		byte[] keyBytes = secretKey.getBytes(); // Convertit la chaîne en octets
		if (keyBytes.length < 32) { // Vérifie la longueur en octets
			throw new IllegalArgumentException("JWT secret key must be at least 32 bytes (256 bits) for HS256 algorithm. Current key length: " + keyBytes.length);
		}
		return new SecretKeySpec(keyBytes, MacAlgorithm.HS256.getName());
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(
				secretKey.getBytes(), 0, secretKey.getBytes().length, "RSA");
		return NimbusJwtDecoder.withSecretKey(secretKeySpec)
				.macAlgorithm(MacAlgorithm.HS256).build();
	}




	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plus(1, ChronoUnit.DAYS))
				.subject(authentication.getName())
				.build();
		JwtEncoder encoder = jwtEncoder();
		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(), claims);
		String token = encoder.encode(jwtEncoderParameters).getTokenValue();
		return token;
	}

//	public boolean validateToken(String token){
//		try{
//			SignedJWT signedJWT = SignedJWT.parse(token);
//			JWSVerifier verifier = new MACVerifier(secretKey);
//					return signedJWT.verify(verifier);
//		}catch (ParseException | JOSEException e){
//			return false;
//		}
//	}

	// Utilise le JwtDecoder de Spring Security pour la validation et l'extraction
	public boolean validateToken(String token) {
		try {
			jwtDecoder().decode(token); // Si la décoction réussit, le token est valide
			return true;
		} catch (JwtException e) {
			// Log l'exception pour le débogage
			logger.warn("JWT validation failed: " + e.getMessage());
			return false;
		}
	}

	public  String extractUsernameFromToken(String token){
		return jwtDecoder().decode(token).getSubject();

	}

}
