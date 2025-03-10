package com.alierensevinc.dwallet.service;

import com.alierensevinc.dwallet.dto.DepositRequest;
import com.alierensevinc.dwallet.dto.WithdrawRequest;
import com.alierensevinc.dwallet.entity.*;
import com.alierensevinc.dwallet.repository.TransactionRepository;
import com.alierensevinc.dwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction deposit(DepositRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        String status = request.getAmount() > 1000 ? "PENDING" : "APPROVED";

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(TransactionOppositeType.valueOf(request.getSourceType().toUpperCase()))
                .oppositeParty(request.getSource())
                .status(request.getAmount() > 1000 ? TransactionStatus.PENDING : TransactionStatus.APPROVED)
                .build();

        transactionRepository.save(transaction);

        if ("APPROVED".equals(status)) {
            wallet.setBalance(wallet.getBalance() + request.getAmount());
            wallet.setUsableBalance(wallet.getUsableBalance() + request.getAmount());
        } else {
            wallet.setBalance(wallet.getBalance() + request.getAmount());
        }

        walletRepository.save(wallet);
        return transaction;
    }

    @Transactional
    public Transaction withdraw(WithdrawRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getUsableBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        String status = request.getAmount() > 1000 ? "PENDING" : "APPROVED";

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(TransactionOppositeType.valueOf(request.getDestinationType().toUpperCase()))
                .oppositeParty(request.getDestination())
                .status(request.getAmount() > 1000 ? TransactionStatus.PENDING : TransactionStatus.APPROVED)
                .build();

        transactionRepository.save(transaction);

        if ("APPROVED".equals(status)) {
            wallet.setBalance(wallet.getBalance() - request.getAmount());
            wallet.setUsableBalance(wallet.getUsableBalance() - request.getAmount());
        } else {
            wallet.setUsableBalance(wallet.getUsableBalance() - request.getAmount());
        }

        walletRepository.save(wallet);
        return transaction;
    }
}