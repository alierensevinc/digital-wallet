package com.alierensevinc.dwallet.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String tckn;
    private String password;
}