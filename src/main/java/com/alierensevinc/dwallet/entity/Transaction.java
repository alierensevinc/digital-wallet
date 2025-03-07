package com.alierensevinc.dwallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    private double amount;
    private String type;  // DEPOSIT or WITHDRAW
    private String oppositePartyType; // IBAN or PAYMENT
    private String oppositeParty;
    private String status; // PENDING, APPROVED, DENIED
}