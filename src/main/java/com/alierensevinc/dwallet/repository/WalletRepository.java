package com.alierensevinc.dwallet.repository;

import com.alierensevinc.dwallet.entity.Customer;
import com.alierensevinc.dwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByCustomer(Customer customer);
}