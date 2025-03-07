package com.alierensevinc.dwallet.config;

import com.alierensevinc.dwallet.entity.Customer;
import com.alierensevinc.dwallet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/auth/login").permitAll() // Allow H2 Console & Login
                        .requestMatchers(HttpMethod.POST, "/wallets/create").hasRole("EMPLOYEE") // Only employees can create wallets
                        .requestMatchers(HttpMethod.GET, "/wallets/**").hasAnyRole("CUSTOMER", "EMPLOYEE") // Customers can see their wallets, employees can see all
                        .requestMatchers(HttpMethod.POST, "/transactions/deposit").hasRole("CUSTOMER") // Only customers can deposit
                        .requestMatchers(HttpMethod.POST, "/transactions/withdraw").hasRole("CUSTOMER") // Only customers can withdraw
                        .requestMatchers(HttpMethod.POST, "/transactions/approve").hasRole("EMPLOYEE") // Only employees can approve transactions
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
                .headers(headers -> headers.frameOptions().disable()) // Allow H2 Console frames
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)) // Stateless
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Apply JWT filter

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository) {
        return username -> {
            Customer customer = customerRepository.findByTckn(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return User.withUsername(customer.getTckn())
                    .password("{noop}password") // Keep for testing, replace with encoded passwords in production
                    .roles(customer.getRole().name()) // Assign ROLE_CUSTOMER or ROLE_EMPLOYEE
                    .build();
        };
    }
}