package com.alierensevinc.dwallet.controller;

import com.alierensevinc.dwallet.dto.WalletRequest;
import com.alierensevinc.dwallet.entity.Wallet;
import com.alierensevinc.dwallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletRequest request) {
        Wallet wallet = walletService.createWallet(request.getCustomerId(), request.getWalletName(),
                request.getCurrency(), request.isActiveForShopping(), request.isActiveForWithdraw());
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Wallet>> getWallets(@PathVariable Long customerId) {
        return ResponseEntity.ok(walletService.getWalletsByCustomer(customerId));
    }
}