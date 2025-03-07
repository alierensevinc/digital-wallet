package com.alierensevinc.dwallet.service;

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

    public Wallet createWallet(Long customerId, String walletName, String currency, boolean activeForShopping, boolean activeForWithdraw) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!List.of("TRY", "USD", "EUR").contains(currency)) {
            throw new RuntimeException("Invalid currency");
        }

        Wallet wallet = new Wallet(null, customer, walletName, currency, activeForShopping, activeForWithdraw, 0.0, 0.0);
        return walletRepository.save(wallet);
    }

    public List<Wallet> getWalletsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return walletRepository.findByCustomer(customer);
    }
}