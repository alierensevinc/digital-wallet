package com.alierensevinc.dwallet.service;

import com.alierensevinc.dwallet.entity.Transaction;
import com.alierensevinc.dwallet.entity.Wallet;
import com.alierensevinc.dwallet.repository.TransactionRepository;
import com.alierensevinc.dwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public Transaction deposit(Long walletId, double amount, String source, String sourceType) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        String status = amount > 1000 ? "PENDING" : "APPROVED";

        Transaction transaction = new Transaction(null, wallet, amount, "DEPOSIT", sourceType, source, status);
        transactionRepository.save(transaction);

        if (status.equals("APPROVED")) {
            wallet.setBalance(wallet.getBalance() + amount);
            wallet.setUsableBalance(wallet.getUsableBalance() + amount);
        } else {
            wallet.setBalance(wallet.getBalance() + amount);
        }

        walletRepository.save(wallet);
        return transaction;
    }
}