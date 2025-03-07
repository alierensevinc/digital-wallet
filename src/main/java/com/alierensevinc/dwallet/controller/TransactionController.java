package com.alierensevinc.dwallet.controller;

import com.alierensevinc.dwallet.dto.DepositRequest;
import com.alierensevinc.dwallet.entity.Transaction;
import com.alierensevinc.dwallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(request.getWalletId(), request.getAmount(),
                request.getSource(), request.getSourceType()));
    }
}