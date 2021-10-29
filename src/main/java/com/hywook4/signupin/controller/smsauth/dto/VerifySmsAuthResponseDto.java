package com.hywook4.signupin.controller.smsauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifySmsAuthResponseDto {
    private String name;
    private String phoneNumber;
    private String verifiedToken;
}
