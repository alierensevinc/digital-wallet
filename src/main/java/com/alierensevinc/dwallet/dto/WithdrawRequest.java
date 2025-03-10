package com.alierensevinc.dwallet.dto;

import lombok.Data;

@Data
public class WithdrawRequest {
    private Long walletId;
    private double amount;
    private String destination;
    private String destinationType;
}