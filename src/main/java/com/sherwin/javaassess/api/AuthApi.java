package com.sherwin.javaassess.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sherwin.javaassess.jwt.JwtTokenUtil;
import com.sherwin.javaassess.models.User;

@RestController
public class AuthApi {
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JwtTokenUtil jwtUtil;
	
	
	@GetMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request){
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), 					request.getPassword())
					);
			
			User user = (User) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);
			AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
			
			return ResponseEntity.ok(response);
			
		}catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}

}
