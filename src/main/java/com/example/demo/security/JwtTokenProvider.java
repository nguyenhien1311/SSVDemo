package com.example.demo.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.exception.CustomException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	@Value("${secret-key}")
	private String secret_key;
	
	@Value("${expirationMs}")
	private int expirationMs;
	
	@Value("${refreshExpirationMs}")
	private int refreshExpirationMs;
	
	public String generateToken(Authentication  authentication) {
		UserDetail userDetail = (UserDetail) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject(userDetail.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(SignatureAlgorithm.HS512, secret_key)
				.compact();
	}
	public String generateRefreshToken(Authentication  authentication) {
		UserDetail userDetail = (UserDetail) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject(userDetail.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
				.signWith(SignatureAlgorithm.HS512, secret_key)
				.compact();
	}
	
	public String getUserNameFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secret_key)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secret_key).parseClaimsJws(authToken);
			return true;	
		} catch (SignatureException e) {
			throw new CustomException("Invalid JWT signature: " + e.getMessage());
		} catch (MalformedJwtException e) {
			throw new CustomException("Invalid JWT token: {}"+ e.getMessage());
		} catch (ExpiredJwtException e) {
			throw new CustomException("JWT token is expired: {}"+ e.getMessage());
		} catch (UnsupportedJwtException e) {
			throw new CustomException("JWT token is unsupported: {}"+ e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new CustomException("JWT claims string is empty: {}"+ e.getMessage());
		}
	}
	
	
}
