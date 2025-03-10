package com.alierensevinc.dwallet.controller;

import com.alierensevinc.dwallet.dto.WalletRequest;
import com.alierensevinc.dwallet.entity.Wallet;
import com.alierensevinc.dwallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_CUSTOMER"))) {
            // A CUSTOMER can only create a wallet for themselves
            request.setCustomerId(Long.valueOf(username));
        }

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_EMPLOYEE"))) {
            // Prevent an EMPLOYEE from creating a wallet for themselves
            if (request.getCustomerId() != null && request.getCustomerId().equals(Long.valueOf(username))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }


        Wallet wallet = walletService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<List<Wallet>> getWallets(@PathVariable Long customerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // TCKN from JWT

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("CUSTOMER"))) {
            // Customers can only retrieve their own wallets
            if (!customerId.equals(Long.valueOf(username))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(walletService.getWalletsByCustomer(customerId));
    }
}