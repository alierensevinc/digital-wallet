package com.alierensevinc.dwallet.dto;

import lombok.Data;

@Data
public class DepositRequest {
    private Long walletId;
    private double amount;
    private String source;
    private String sourceType;
}