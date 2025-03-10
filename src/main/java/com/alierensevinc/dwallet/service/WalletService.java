package com.alierensevinc.dwallet.service;

import com.alierensevinc.dwallet.dto.WalletRequest;
import com.alierensevinc.dwallet.entity.Customer;
import com.alierensevinc.dwallet.entity.Wallet;
import com.alierensevinc.dwallet.repository.CustomerRepository;
import com.alierensevinc.dwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;

    public Wallet createWallet(WalletRequest walletRequest) {
        Customer customer = customerRepository.findById(walletRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!List.of("TRY", "USD", "EUR").contains(walletRequest.getCurrency())) {
            throw new RuntimeException("Invalid currency");
        }

        Wallet wallet = new Wallet(null, customer, walletRequest.getWalletName(), walletRequest.getCurrency(), walletRequest.isActiveForShopping(), walletRequest.isActiveForWithdraw(), 0.0, 0.0);
        return walletRepository.save(wallet);
    }

    public List<Wallet> getWalletsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return walletRepository.findByCustomer(customer);
    }
}