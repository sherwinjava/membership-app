package com.sherwin.javaassess.jwt;

import java.util.Date;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sherwin.javaassess.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(JwtTokenUtil.class);
	private static  final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; //24h
	
	@Value("${app.jwt.secret}")
	private String secretKey;
	
	public String generateAccessToken (User user) {
		return Jwts.builder()
				.setSubject(user.getId()+","+user.getEmail())
				.claim("roles",user.getRoles().toString())
				.setIssuer("CodeJava")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();			
						
	}	
	
	public boolean validateAccessToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);			
			return true;
		}catch (ExpiredJwtException ex) {
			LOGGER.error("JWT expired", ex);
		}catch (IllegalArgumentException ex) {
			LOGGER.error("Token is null, empty or has only whitespace", ex);
		}catch (MalformedJwtException ex) {
			LOGGER.error("JWT is invalid", ex);
		}catch (UnsupportedJwtException ex) {
			LOGGER.error("JTW is not supported", ex);
		}catch (SignatureException ex) {
			LOGGER.error("Signature validation failed", ex);
		}
		return false;
	}
	
	
	public String getSubject(String token) {
		return parseClaims(token).getSubject();
	}
	
	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}
	


}
