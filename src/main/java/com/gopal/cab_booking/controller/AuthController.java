package com.gopal.cab_booking.controller;

import com.gopal.cab_booking.dto.ApiResponse;
import com.gopal.cab_booking.dto.LoginRequest;
import com.gopal.cab_booking.dto.LoginResponse;
import com.gopal.cab_booking.dto.RegisterDriverRequest;
import com.gopal.cab_booking.dto.RegisterRiderRequest;
import com.gopal.cab_booking.dto.UserResponse;
import com.gopal.cab_booking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/rider")
    public ResponseEntity<ApiResponse<Void>> registerRider(
            @Valid @RequestBody RegisterRiderRequest request) {
        authService.registerRider(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Rider registered successfully", null));
    }

    @PostMapping("/register/driver")
    public ResponseEntity<ApiResponse<Void>> registerDriver(
            @Valid @RequestBody RegisterDriverRequest request) {
        authService.registerDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Driver registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(Authentication authentication) {
        UserResponse user = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Current user", user));
    }
}