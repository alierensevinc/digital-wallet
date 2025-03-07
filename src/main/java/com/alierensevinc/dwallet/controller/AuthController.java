package com.alierensevinc.dwallet.controller;

import com.alierensevinc.dwallet.dto.AuthResponse;
import com.alierensevinc.dwallet.dto.LoginRequest;
import com.alierensevinc.dwallet.entity.Customer;
import com.alierensevinc.dwallet.repository.CustomerRepository;
import com.alierensevinc.dwallet.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Customer customer = customerRepository.findByTckn(request.getTckn())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!request.getPassword().equals("password")) { // Replace with hashed password in production
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(customer.getTckn(), customer.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}



