package com.hywook4.signupin.controller.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignInResponseDto {
    private String email;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String jwtToken;
}
