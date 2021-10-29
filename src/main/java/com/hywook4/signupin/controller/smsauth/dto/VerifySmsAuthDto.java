package com.hywook4.signupin.controller.smsauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifySmsAuthDto {
    private String name;
    private String phoneNumber;
    private String verificationCode;
}
