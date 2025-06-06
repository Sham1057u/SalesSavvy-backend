package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "https://sales-savvy-frontend.vercel.app", allowCredentials = "true")
@RequestMapping("/api/auth")

public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
        	User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        	String token = authService.generateToken(user);

        	// Remove this if you're using manual header method
        	// Cookie cookie = new Cookie("authToken", token);
        	// cookie.setHttpOnly(true);
        	// cookie.setSecure(true);
        	// cookie.setPath("/");
        	// cookie.setMaxAge(3600); // 1 hour
        	// cookie.setDomain("salessavvy-backend-c2yc.onrender.com"); // Optional, not needed
        	// cookie.sameSite("None");
        	// response.addCookie(cookie);

        	// ✅ Recommended way (manual header for full control)
        	response.addHeader("Set-Cookie",
        	    "authToken=" + token +
        	    "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=3600");


            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("role", user.getRole().name());
            responseBody.put("username", user.getUsername());

            return ResponseEntity.ok(responseBody);
            
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,HttpServletResponse response) {
        try {
        	User user=(User) request.getAttribute("authenticatedUser");
            authService.logout(user);
            Cookie cookie = new Cookie("authToken", null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Logout successful");
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Logout failed");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
}
