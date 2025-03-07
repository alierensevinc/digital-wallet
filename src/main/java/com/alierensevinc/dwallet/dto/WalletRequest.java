package com.alierensevinc.dwallet.dto;

import lombok.Data;

@Data
public class WalletRequest {
    private Long customerId;
    private String walletName;
    private String currency;
    private boolean activeForShopping;
    private boolean activeForWithdraw;
}